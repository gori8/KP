package rs.ac.uns.ftn.paypal.cmrs;


import com.paypal.api.payments.*;
import com.paypal.api.payments.Currency;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rs.ac.uns.ftn.paypal.dto.*;
import rs.ac.uns.ftn.paypal.utils.MyPaymentUtils;
import rs.ac.uns.ftn.url.AmountAndUrlDTO;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.*;

@Service
public class PaymentServiceImpl implements PaymentService{

    private static final String PAYPAL_CLIENTID = "AbzohpnhZ3DCLLCVhbI7OlW3wpsqZdSEGql5YXnhh01S4KUKXo088-YkOv6E5HzY6arAxmYhkL8hiaI4";
    private static final String PAYPAL_SECRET = "ED-XvdfrGw8IWJKlmHhZuzLJbtm84ZT4Keo6f2cCC74H_lx80EF_us8ujQNhEgzsiyTIhJKSFVNG6jlj";
    private static final int PAYPAL_APPROVAL_URL_INDEX = 1;
    private final Logger LOGGER = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private static final String INTENT = "authorize";
    private static final String MODE = "sandbox";
    private static final String PAYMENT_METHOD = "paypal";
    private static final String PLAN_TYPE="INFINITE";
    private static final String INITIAL_FAIL_AMOUNT_ACTION="CONTINUE";
    private static final String PAYMENT_DEF_TYPE="REGULAR";
    private static final String PLAN_STATE="CREATE";
    private static final String PLAN_FREQUENCY="MONTH";
    private static final String SUBSCRIPTION_CANCEL="https://localhost:8771/paypal/api/paypal/subscription/cancel";
    static final String SUBSCRIPTION_CONFIRM="https://localhost:8771/paypal/api/paypal/subscription/confirm";


    @Autowired
    MyPaymentRepository myPaymentRepository;

    @Autowired
    SellerRepository sellerRepository;

    @Autowired
    RestTemplate restTemplate;

    @Override
    public CreatePaymentOrSubResponse createPayment(CreatePaymentOrSubRequest request) {

        System.out.println("UUID: "+request.getCasopisUuid());

        AmountAndUrlDTO amountAndUrlDTO=MyPaymentUtils.getAmountAndRedirectUrl(restTemplate,request.getCasopisUuid());

        BigDecimal amount = amountAndUrlDTO.getAmount();
        String redirectUrl = amountAndUrlDTO.getRedirectUrl();
        String sellerEmail = amountAndUrlDTO.getSellerEmail();

        Seller seller = sellerRepository.findBySellerEmail(sellerEmail);

        MyPayment myPayment=new MyPayment();

        myPayment.setItemId(UUID.fromString(request.getCasopisUuid()));

        myPayment=myPaymentRepository.save(myPayment);

        Transaction transaction = MyPaymentUtils.setTransaction(amount, seller.getPaypalEmail());

        List<Transaction> transactions = new ArrayList<>();

        transactions.add(transaction);

        Payment payment = new Payment();
        payment.setIntent(INTENT);
        payment.setPayer(MyPaymentUtils.generatePayer(PAYMENT_METHOD));
        payment.setTransactions(transactions);

        payment.setRedirectUrls(MyPaymentUtils.setRedirectUrls(myPayment.getId()));

        APIContext apiContext = new APIContext(PAYPAL_CLIENTID, PAYPAL_SECRET, MODE);

        Payment createdPayment = null;
        PayPalResponseDTO ppr=new PayPalResponseDTO();
        try {
            createdPayment = payment.create(apiContext);

            ppr.setApprovalUrl(createdPayment.getLinks().get(PAYPAL_APPROVAL_URL_INDEX).getHref());
            ppr.setId(createdPayment.getId());


        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }

        myPayment.setAmount(amount);
        myPayment.setSeller(seller);
        myPayment.setPaymentId(createdPayment.getId());
        myPayment.setRedirectUrl(redirectUrl);
        myPaymentRepository.save(myPayment);


        return new CreatePaymentOrSubResponse(ppr.getApprovalUrl(),ppr.getId());
    }

    @Override
    public String executePayment(ExecutePaymentRequest request) {
        String paymentId = request.getPaymentID();
        String payerId = request.getPayerID();
        String token = request.getToken();
        MyPayment myPayment = myPaymentRepository.findByPaymentId(paymentId);

        APIContext apiContext = new APIContext(PAYPAL_CLIENTID, PAYPAL_SECRET, MODE);

        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        try {
            Payment executedPayment = payment.execute(apiContext, paymentExecution);

            Authorization authorization = executedPayment.getTransactions().get(0).getRelatedResources().get(0).getAuthorization();

            Amount amount = MyPaymentUtils.setAmount(myPayment.getAmount());

            Capture capture = new Capture();
            capture.setAmount(amount);

            capture.setIsFinalCapture(true);

            Capture responseCapture = authorization.capture(apiContext, capture);

            LOGGER.info("Capture id=" + responseCapture.getId() + " and status=" + responseCapture.getState());

            myPayment.setSuccessful(true);
            myPaymentRepository.save(myPayment);

            LOGGER.info("Executed payment - Request: \n" + Payment.getLastRequest());
            LOGGER.info("Executed payment - Response: \n" + Payment.getLastResponse());

        } catch (PayPalRESTException e) {
            myPayment.setSuccessful(false);
            myPaymentRepository.save(myPayment);
            return notifyNc(myPayment.getRedirectUrl()+"/false");
        }

        return notifyNc(myPayment.getRedirectUrl()+"/true");
    }

    @Override
    public String cancelPayment(Long id) {
        MyPayment payment = myPaymentRepository.getOne(id);
        payment.setSuccessful(false);
        myPaymentRepository.save(payment);
        return payment.getRedirectUrl();
    }


    private String notifyNc(String url){
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity=new HttpEntity("",headers);
        ResponseEntity<String> redirectUrl=restTemplate.postForEntity(url,entity,String.class);
        return redirectUrl.getBody();
    }


    @Override
    public Plan createBillingPlan(CreatePaymentOrSubRequest request, AmountAndUrlDTO amountAndUrlDTO) {

        BigDecimal amount=amountAndUrlDTO.getAmount();
        String redirectUrl=amountAndUrlDTO.getRedirectUrl();
        String sellerEmail = amountAndUrlDTO.getSellerEmail();

        MyPayment myPayment=new MyPayment();

        Plan plan = new Plan();

        Date date = new Date();
        String strDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
        plan.setCreateTime(strDate);

        plan.setDescription("Subscription for item "+request.getCasopisUuid());

        plan.setName("Item "+request.getCasopisUuid()+" plan");

        plan.setType(PLAN_TYPE);
        plan.setState(PLAN_STATE);

        MerchantPreferences merchantPreferences=new MerchantPreferences();
        merchantPreferences.setMaxFailAttempts("3");
        merchantPreferences.setCancelUrl(SUBSCRIPTION_CANCEL);
        merchantPreferences.setReturnUrl(SUBSCRIPTION_CONFIRM);
        merchantPreferences.setInitialFailAmountAction(INITIAL_FAIL_AMOUNT_ACTION);

        PaymentDefinition paymentDefinition = new PaymentDefinition();
        paymentDefinition.setName("Regular payment definition for "+request.getCasopisUuid());
        paymentDefinition.setType(PAYMENT_DEF_TYPE);
        paymentDefinition.setCycles("0");
        Currency currency=new com.paypal.api.payments.Currency();
        currency.setCurrency("EUR");
        currency.setValue(amount.toString());
        paymentDefinition.setAmount(currency);
        paymentDefinition.setFrequencyInterval("6");
        paymentDefinition.setFrequency(PLAN_FREQUENCY);

        List<PaymentDefinition> paymentDefinitions=new ArrayList<>();
        paymentDefinitions.add(paymentDefinition);

        plan.setMerchantPreferences(merchantPreferences);
        plan.setPaymentDefinitions(paymentDefinitions);

        APIContext apiContext = new APIContext(PAYPAL_CLIENTID, PAYPAL_SECRET, MODE);
        CreatePaymentOrSubResponse response=new CreatePaymentOrSubResponse();
        Plan createdPlan = null;
        try {
            createdPlan = plan.create(apiContext);
            List<Patch> patchRequestList = new ArrayList<Patch>();
            Map<String, String> value = new HashMap<String, String>();
            value.put("state", "ACTIVE");

            // Create update object to activate plan
            Patch patch = new Patch();
            patch.setPath("/");
            patch.setValue(value);
            patch.setOp("replace");
            patchRequestList.add(patch);

            // Activate plan
            createdPlan.update(apiContext, patchRequestList);
            LOGGER.info("Plan state = " + createdPlan.getState());
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }

        return createdPlan;
    }

    @Override
    public String activateSubscription(CreatePaymentOrSubRequest request) {
        AmountAndUrlDTO amountAndUrlDTO=MyPaymentUtils.getAmountAndRedirectUrl(restTemplate,request.getCasopisUuid());

        BigDecimal amount=amountAndUrlDTO.getAmount();
        String redirectUrl=amountAndUrlDTO.getRedirectUrl();
        String sellerEmail = amountAndUrlDTO.getSellerEmail();

        Seller seller = sellerRepository.findBySellerEmail(sellerEmail);

        if(seller.getCanSubscribe()){
            String planId = seller.getPlanId();
            if (planId==null){
                Plan plan = createBillingPlan(request,amountAndUrlDTO);
                planId=plan.getId();
            }

            Agreement agreement = new Agreement();
            agreement.setName("Subscription for plan "+planId);
            agreement.setDescription("Subscription for item "+request.getCasopisUuid());
            Date now = new Date();
            String agreementDate = now.toInstant().plus(2, ChronoUnit.DAYS).toString();
            agreement.setStartDate(agreementDate);

            Plan plan = new Plan();
            plan.setId(planId);
            agreement.setPlan(plan);

            Payer payer = new Payer();
            payer.setPaymentMethod("paypal");
            agreement.setPayer(payer);

            APIContext apiContext = new APIContext(PAYPAL_CLIENTID, PAYPAL_SECRET, MODE);

            try {
                agreement = agreement.create(apiContext);

                for (Links links : agreement.getLinks()) {
                    if ("approval_url".equals(links.getRel())) {
                        return links.getHref();
                        //REDIRECT USER TO url
                    }
                }
            } catch (PayPalRESTException e) {
                System.err.println(e.getDetails());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


        }

        return null;
    }

    @Override
    public void executeSubAgreement(String token) {

        Agreement agreement =  new Agreement();
        agreement.setToken(token);
        APIContext apiContext = new APIContext(PAYPAL_CLIENTID, PAYPAL_SECRET, MODE);

        try {
            Agreement activeAgreement = agreement.execute(apiContext, agreement.getToken());
            LOGGER.info("Agreement created with ID " + activeAgreement.getId());
        } catch (PayPalRESTException e) {
            LOGGER.error(e.getMessage());
        }
    }


}
