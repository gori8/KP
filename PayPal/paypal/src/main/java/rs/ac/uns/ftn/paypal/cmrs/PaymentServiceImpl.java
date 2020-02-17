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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rs.ac.uns.ftn.paypal.dto.*;
import rs.ac.uns.ftn.paypal.utils.MyPaymentUtils;
import rs.ac.uns.ftn.url.*;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
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
    private static final String SUBSCRIPTION_CANCEL="https://192.168.43.161:8771/paypal/api/paypal/subscription/cancel";
    static final String SUBSCRIPTION_CONFIRM="https://192.168.43.161:8771/paypal/api/paypal/subscription/confirm";

    private static final APIContext API_CONTEXT = new APIContext(PAYPAL_CLIENTID, PAYPAL_SECRET, MODE);



    @Autowired
    MyPaymentRepository myPaymentRepository;

    @Autowired
    SellerRepository sellerRepository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    SubPlanRepository subPlanRepository;


    @Override
    public CreatePaymentOrSubResponse createPayment(CreatePaymentOrSubRequest request) {

        System.out.println("UUID: "+request.getCasopisUuid());

        AmountAndUrlDTO amountAndUrlDTO=MyPaymentUtils.getAmountAndRedirectUrl(restTemplate,request.getCasopisUuid());

        System.out.println("Email: "+amountAndUrlDTO.getSellerEmail());


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


        Payment createdPayment = null;
        PayPalResponseDTO ppr=new PayPalResponseDTO();
        try {
            createdPayment = payment.create(API_CONTEXT);

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


        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        try {
            Payment executedPayment = payment.execute(API_CONTEXT, paymentExecution);

            Authorization authorization = executedPayment.getTransactions().get(0).getRelatedResources().get(0).getAuthorization();

            Amount amount = MyPaymentUtils.setAmount(myPayment.getAmount());

            Capture capture = new Capture();
            capture.setAmount(amount);

            capture.setIsFinalCapture(true);

            Capture responseCapture = authorization.capture(API_CONTEXT, capture);

            LOGGER.info("Capture id=" + responseCapture.getId() + " and status=" + responseCapture.getState());

            myPayment.setStatus(PayPalPaymentStatus.SUCCESSFUL);
            myPaymentRepository.save(myPayment);

            LOGGER.info("Executed payment - Request: \n" + Payment.getLastRequest());
            LOGGER.info("Executed payment - Response: \n" + Payment.getLastResponse());

        } catch (PayPalRESTException e) {
            myPayment.setStatus(PayPalPaymentStatus.ERROR);
            myPaymentRepository.save(myPayment);
            return notifyNc(myPayment.getRedirectUrl()+"/false");
        }

        return notifyNc(myPayment.getRedirectUrl()+"/true");
    }

    @Override
    public String cancelPayment(Long id) {
        MyPayment payment = myPaymentRepository.getOne(id);
        payment.setStatus(PayPalPaymentStatus.CANCELED);
        myPaymentRepository.save(payment);
        return notifyNc(payment.getRedirectUrl()+"/false");
    }

    @Override
    public void cancelSubscription(Long id) {
        Subscription subscription = subscriptionRepository.getOne(id);
        subscription.setStatus(SubscriptionStatus.CANCELED);
        subscriptionRepository.save(subscription);
    }


    private String notifyNc(String url){
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity=new HttpEntity("",headers);
        ResponseEntity<String> redirectUrl=restTemplate.postForEntity(url,entity,String.class);
        return redirectUrl.getBody();
    }

    private String notifyNcSubscription(Subscription subscription, String url, Date date){
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        SubDateDTO subDateDTO = new SubDateDTO();
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        subDateDTO.setDate(simpleDateFormat.format(date));
        HttpEntity entity=new HttpEntity(subDateDTO,headers);
        ResponseEntity<SubRedirectUrlDTO> redirectUrl=restTemplate.postForEntity(url,entity,SubRedirectUrlDTO.class);
        subscription.setOtherAppSubId(redirectUrl.getBody().getPretplataId());
        subscriptionRepository.save(subscription);
        return redirectUrl.getBody().getRedirectUrl();
    }


    @Override
    public Plan createBillingPlan(CreatePaymentOrSubRequest request, AmountAndUrlDTO amountAndUrlDTO, BigDecimal cena, String finishRedirectUrl) {

        BigDecimal amount=cena;
        String sellerEmail = amountAndUrlDTO.getSellerEmail();

        LOGGER.info(String.format("Creating plan: %s   %s   %s",request.getTipCiklusa(),request.getPeriod(),request.getBrojCiklusa()));

        Seller seller = sellerRepository.findBySellerEmail(sellerEmail);
        Subscription subscription=new Subscription();
        subscription.setItemId(UUID.fromString(request.getCasopisUuid()));
        subscription.setAmount(amount);
        subscription.setCycles(request.getBrojCiklusa());
        subscription.setFrequency(request.getPeriod());
        subscription.setFrequencyInterval(request.getUcestalostPerioda());
        subscription.setSeller(seller);
        subscription.setType(request.getTipCiklusa());
        subscription.setRedirectUrl(finishRedirectUrl);

        subscription=subscriptionRepository.save(subscription);

        Plan plan = new Plan();

        Date date = new Date();
        String strDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
        plan.setCreateTime(strDate);

        plan.setDescription("Subscription for item "+request.getCasopisUuid());

        plan.setName("Item "+request.getCasopisUuid()+" plan");

        plan.setType(request.getTipCiklusa());
        plan.setState(PLAN_STATE);

        MerchantPreferences merchantPreferences=new MerchantPreferences();
        merchantPreferences.setMaxFailAttempts("3");
        merchantPreferences.setCancelUrl(SUBSCRIPTION_CANCEL+"/"+subscription.getId());
        merchantPreferences.setReturnUrl(SUBSCRIPTION_CONFIRM+"/"+subscription.getId());
        merchantPreferences.setInitialFailAmountAction(INITIAL_FAIL_AMOUNT_ACTION);

        PaymentDefinition paymentDefinition = new PaymentDefinition();
        paymentDefinition.setName("Regular payment definition for "+request.getCasopisUuid());
        paymentDefinition.setType(PAYMENT_DEF_TYPE);
        paymentDefinition.setCycles(request.getBrojCiklusa().toString());
        Currency currency=new com.paypal.api.payments.Currency();
        currency.setCurrency("USD");
        currency.setValue(amount.toString());
        paymentDefinition.setAmount(currency);
        paymentDefinition.setFrequencyInterval(request.getUcestalostPerioda().toString());
        paymentDefinition.setFrequency(request.getPeriod());

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

        subscription.setPlanId(createdPlan.getId());
        subscriptionRepository.save(subscription);

        return createdPlan;
    }

    @Override
    public String activateSubscription(SubPlanDTO subPlanRequest) {
        CreatePaymentOrSubRequest request = new CreatePaymentOrSubRequest();
        SubPlan subPlan = subPlanRepository.findOneByPlanId(UUID.fromString(subPlanRequest.getPlanId()));
        if(subPlanRequest.getType().equals("INFINITE")) {
            request.setBrojCiklusa(0L);
        }else{
            request.setBrojCiklusa(subPlanRequest.getNumCycles());
        }
        request.setCasopisUuid(subPlan.getItemUuid().toString());
        request.setTipCiklusa(subPlanRequest.getType());
        request.setPeriod(subPlan.getPeriod());
        request.setUcestalostPerioda(subPlan.getUcestalostPerioda().longValue());

        AmountAndUrlDTO amountAndUrlDTO=MyPaymentUtils.getAmountAndRedirectUrl(restTemplate,request.getCasopisUuid());

        String sellerEmail = amountAndUrlDTO.getSellerEmail();

        Seller seller = sellerRepository.findBySellerEmail(sellerEmail);

        if(seller.getCanSubscribe()){
            Plan billingPlan = createBillingPlan(request,amountAndUrlDTO,subPlan.getCena(),subPlan.getRedirectUrl());
            String planId=billingPlan.getId();


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
    public String executeSubAgreement(Long subscriptionId, String token) {
        Subscription subscription = subscriptionRepository.getOne(subscriptionId);
        Agreement agreement =  new Agreement();
        agreement.setToken(token);
        APIContext apiContext = new APIContext(PAYPAL_CLIENTID, PAYPAL_SECRET, MODE);

        try {
            Agreement activeAgreement = agreement.execute(apiContext, agreement.getToken());
            subscription.setAgreementId(activeAgreement.getId());
            subscription.setStatus(SubscriptionStatus.ACTIVE);
            subscriptionRepository.save(subscription);
            LOGGER.info("Agreement created with ID " + activeAgreement.getId());

            Date date = new Date();
            if(subscription.getCycles()!=0L) {
                Long dayMilis = 1000L * 60 * 60 * 24;

                switch (subscription.getFrequency()) {
                    case "DAY": {
                        date = new Date(date.getTime() + (dayMilis * (subscription.getFrequencyInterval() * subscription.getCycles() + 2)));
                    }
                    break;
                    case "WEEK": {
                        date = new Date(date.getTime() + (dayMilis * (subscription.getFrequencyInterval() * subscription.getCycles() * 7 + 2)));
                    }
                    break;
                    case "MONTH": {
                        date = new Date(date.getTime() + (dayMilis * (subscription.getFrequencyInterval() * subscription.getCycles() * 30 + 2)));
                    }
                    break;
                    case "YEAR": {
                        date = new Date(date.getTime() + (dayMilis * (subscription.getFrequencyInterval() * subscription.getCycles() * 365 + 2)));
                    }
                    break;
                }

            }else{
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                date = simpleDateFormat.parse("9999-12-12");
            }

            return notifyNcSubscription(subscription,subscription.getRedirectUrl()+"/true",date);
        } catch (PayPalRESTException | ParseException e) {
            LOGGER.error(e.getMessage());
            return "error";
        }
    }

    @Override
    public void updateStatusOrRetryCapture() {

        LOGGER.info("Checking if all payments have been captured...");
        List<MyPayment> payments = myPaymentRepository.findAllByStatus(PayPalPaymentStatus.CREATED);
        payments.parallelStream().forEach(myPayment -> {
            Payment payment = null;
            try {
                payment = Payment.get(API_CONTEXT, myPayment.getPaymentId());

                LOGGER.info("Payment id: "+payment.getId());
                LOGGER.info("Payment state: "+payment.getState());
                String state = payment.getState().toUpperCase();
                if (state.equalsIgnoreCase("APPROVED")){
                    if (payment.getTransactions().get(0).getRelatedResources().isEmpty()) {
                        LOGGER.info("Trying to recapture payment with paypal paymentId: " + payment.getId());
                        ExecutePaymentRequest request = new ExecutePaymentRequest();
                        request.setPayerID(payment.getPayer().getPayerInfo().getPayerId());
                        request.setPaymentID(payment.getId());
                        executePayment(request);
                        return;
                    }
                    Authorization authorization=payment.getTransactions().get(0).getRelatedResources().get(0).getAuthorization();
                    authorization = Authorization.get(API_CONTEXT,authorization.getId());
                    if(authorization.getState().equalsIgnoreCase("CAPTURED")){
                        myPayment.setStatus(PayPalPaymentStatus.SUCCESSFUL);
                        myPaymentRepository.save(myPayment);
                    }else if (authorization.getState().equalsIgnoreCase("AUTHORIZED")) {
                        LOGGER.info("Trying to recapture payment with paypal paymentId: " + payment.getId());
                        ExecutePaymentRequest request = new ExecutePaymentRequest();
                        request.setPayerID(payment.getPayer().getPayerInfo().getPayerId());
                        request.setPaymentID(payment.getId());
                        executePayment(request);
                    }else{
                        LOGGER.info("Setting status to FAILED of payment with paypal paymentId: " + payment.getId());
                        myPayment.setStatus(PayPalPaymentStatus.FAILED);
                        myPaymentRepository.save(myPayment);
                    }
                }
            } catch (PayPalRESTException e) {
                e.printStackTrace();
            }

        });




    }


    @Scheduled(fixedRate = 60000)
    public void changeStatus() {
        updateStatusOrRetryCapture();
    }

    @Override
    public void updateIntegratedSoftwareStatus() {
        LOGGER.info("Checking if payment is recorded on seller software...");
        List<MyPayment> payments = myPaymentRepository.findAllByStatusAndCheckedStatus(PayPalPaymentStatus.SUCCESSFUL,false);
        payments.parallelStream().forEach(payment -> {

            LOGGER.info("Founded payment: " + payment.getId() + " ,status: " + payment.getStatus());
            LOGGER.info("Getting status on url: " + payment.getRedirectUrl());
            ResponseEntity<Boolean> resp = restTemplate.getForEntity(payment.getRedirectUrl(),Boolean.class);
            if(resp.getBody()) {
                payment.setCheckedStatus(true);
                myPaymentRepository.save(payment);
            }else{
                LOGGER.info("Posting status on seller app on url: " + payment.getRedirectUrl());
                restTemplate.postForEntity(payment.getRedirectUrl()+"/true",null,String.class);
            }
        });

    }

    @Scheduled(fixedRate = 60000)
    public void checkIntegratedSoftwareStatus() {
        updateIntegratedSoftwareStatus();
    }

    @Override
    public String createSubPlanUrl(PayPalSubscriptionDTO request){
        SubPlan subPlan = new SubPlan();
        subPlan.setCena(request.getCena());
        subPlan.setPeriod(request.getPeriod());
        subPlan.setUcestalostPerioda(request.getUcestalostPerioda());
        subPlan.setPlanId(UUID.randomUUID());
        subPlan.setItemUuid(UUID.fromString(request.getPlanUuid()));
        subPlan.setRedirectUrl(request.getRedirectUrl());
        subPlan = subPlanRepository.save(subPlan);
        return UrlClass.FRONT_KP+"/plan/"+subPlan.getPlanId();
    }



}
