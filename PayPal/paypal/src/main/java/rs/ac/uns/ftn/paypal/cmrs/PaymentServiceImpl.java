package rs.ac.uns.ftn.paypal.cmrs;


import com.paypal.api.payments.*;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService{

    private static final String PAYPAL_CLIENTID = "AbzohpnhZ3DCLLCVhbI7OlW3wpsqZdSEGql5YXnhh01S4KUKXo088-YkOv6E5HzY6arAxmYhkL8hiaI4";
    private static final String PAYPAL_SECRET = "ED-XvdfrGw8IWJKlmHhZuzLJbtm84ZT4Keo6f2cCC74H_lx80EF_us8ujQNhEgzsiyTIhJKSFVNG6jlj";
    private static final int PAYPAL_APPROVAL_URL_INDEX = 1;
    private final Logger LOGGER = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private static final String INTENT = "authorize";
    private static final String MODE = "sandbox";
    private static final String PAYMENT_METHOD = "paypal";

    @Autowired
    MyPaymentRepository myPaymentRepository;

    @Autowired
    SellerRepository sellerRepository;

    @Override
    public CreatePaymentResponse createPayment(CreatePaymentRequest request) {

        System.out.println("UUID: "+request.getCasopisUuid());

        AmountAndUrlDTO amountAndUrlDTO=MyPaymentUtils.getAmountAndRedirectUrl(request.getCasopisUuid());

        BigDecimal amount=amountAndUrlDTO.getAmount();
        String redirectUrl=amountAndUrlDTO.getRedirectUrl();

        Seller seller = sellerRepository.findByCasopisID(UUID.fromString(request.getCasopisUuid()));

        MyPayment myPayment=new MyPayment();

        myPayment=myPaymentRepository.save(myPayment);

        Transaction transaction = MyPaymentUtils.setTransaction(amount, seller.getEmail());

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
            ppr.setPaymentId(createdPayment.getId());
            System.out.println(ppr.getApprovalUrl());
            System.out.println(ppr.getPaymentId());

        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }

        myPayment.setAmount(amount);
        myPayment.setSeller(seller);
        myPayment.setPaymentId(createdPayment.getId());
        myPayment.setRedirectUrl(redirectUrl);
        MyPayment savedPayment = myPaymentRepository.save(myPayment);


        return new CreatePaymentResponse(ppr.getApprovalUrl(),ppr.getPaymentId());
    }

    @Override
    public String executePayment(ExecutePaymentRequest request) {
        String paymentId = request.getPaymentID();
        String payerId = request.getPayerID();
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


    @Autowired
    RestTemplate restTemplate;

    private String notifyNc(String url){
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity=new HttpEntity("",headers);
        ResponseEntity<String> redirectUrl=restTemplate.postForEntity(url,entity,String.class);
        return "\""+redirectUrl.getBody()+"\"";
    }


}
