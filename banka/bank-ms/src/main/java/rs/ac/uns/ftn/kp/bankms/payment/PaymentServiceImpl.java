package rs.ac.uns.ftn.kp.bankms.payment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import rs.ac.uns.ftn.kp.bankms.client.SellerRepository;
import rs.ac.uns.ftn.kp.bankms.client.SellerService;
import rs.ac.uns.ftn.kp.bankms.model.Seller;
import rs.ac.uns.ftn.kp.bankms.model.Payment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rs.ac.uns.ftn.kp.bankms.model.PaymentStatus;
import rs.ac.uns.ftn.url.AmountAndUrlDTO;
import rs.ac.uns.ftn.url.UrlClass;
import rs.ac.uns.ftn.url.UrlDTO;

import java.util.List;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {


    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private SellerService sellerService;

    @Autowired
    private SellerRepository sellerRepository;

    @Override
    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    private final Logger LOGGER = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Autowired
    RestTemplate restTemplate;


    @Override
    public String handleKpRequest(PaymentRequest kpRequestDto) {


        String fooResourceUrl1
                = UrlClass.DOBAVI_CENU_URL_SA_PAYMENT_INFO + kpRequestDto.getCasopisUuid();

        ResponseEntity<AmountAndUrlDTO> resp
                = restTemplate.getForEntity(fooResourceUrl1, AmountAndUrlDTO.class);


        Payment payment = new Payment();
        payment.setItemUuid(UUID.fromString(kpRequestDto.getCasopisUuid()));
        payment.setAmount(resp.getBody().getAmount());
        payment.setStatus(PaymentStatus.CREATED_ON_KP);
        payment.setSellerEmail(resp.getBody().getSellerEmail());
        payment.setCheckStatusUrl(resp.getBody().getRedirectUrl());

        Payment savedPayment=paymentRepository.save(payment);

        PaymentResponse response1 = new PaymentResponse();


        String fooResourceUrl
                = UrlClass.DOBAVI_REDIRECT_URL_SA_BANKE;

        ObjectMapper mapper = new ObjectMapper();

        MappingClass mc = new MappingClass();
        mc.setAmount(resp.getBody().getAmount());
        mc.setErrorUrl(UrlClass.BANKMS_URL +savedPayment.getId()+"/error");
        mc.setFailedUrl(UrlClass.BANKMS_URL +savedPayment.getId()+ "/failed");
        mc.setSuccessUrl(UrlClass.BANKMS_URL +savedPayment.getId()+ "/successful");

        Seller seller = sellerService.getBySeller(resp.getBody().getSellerEmail());
        mc.setMerchantId(seller.getMerchantId());


        String json="";

        try {
            json = mapper.writeValueAsString(mc);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<String>(json, headers);

        ResponseEntity<String> response
                = restTemplate.postForEntity(fooResourceUrl,entity,String.class);

        String urlJson="";

        try {
            JSONObject jsonObject=new JSONObject(response.getBody());
            urlJson=jsonObject.getString("url");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        savedPayment.setUrl(urlJson);


        savedPayment.setStatus(PaymentStatus.CREATED);

        paymentRepository.save(savedPayment);

        System.out.println(urlJson);
        return urlJson;

    }

    @Override
    public String setPaymentStatus(String id, String status) {
        Payment payment=paymentRepository.getOne(Long.valueOf(id));
        if(payment!=null){
            if(status.equals("error")){
                payment.setStatus(PaymentStatus.ERROR);
                paymentRepository.save(payment);
                ResponseEntity<String> response
                        = restTemplate.postForEntity(payment.getCheckStatusUrl()+"/false",null,String.class);
                return response.getBody();

            }else if(status.equals("failed")){
                payment.setStatus(PaymentStatus.FAILED);
                paymentRepository.save(payment);
                ResponseEntity<String> response
                        = restTemplate.postForEntity(payment.getCheckStatusUrl()+"/false",null,String.class);
                return response.getBody();
            }else if(status.equals("successful")){
                payment.setStatus(PaymentStatus.SUCCESSFUL);
                paymentRepository.save(payment);
                ResponseEntity<String> response
                        = restTemplate.postForEntity(payment.getCheckStatusUrl()+"/true",null,String.class);
                return response.getBody();
            }
        }
        return null;
    }

    @Override
    public void updateStatus() {

        LOGGER.info("Checking if bank status is changed...");
        List<Payment> payments = paymentRepository.findAllByStatus(PaymentStatus.CREATED);
        payments.parallelStream().forEach(payment -> {

            LOGGER.info("Founded payment: " + payment.getId() + " ,status: " + payment.getStatus() + " ,url: " + payment.getUrl());
            String getStatusUrl = "https://localhost:8091/payment/status";
            UrlDTO urlDTO=new UrlDTO(payment.getUrl());
            ResponseEntity<PaymentStatus> resp = restTemplate.postForEntity(getStatusUrl,urlDTO,PaymentStatus.class);
            LOGGER.info("Payment with that url from bank: " + payment.getUrl() + " ,status: " + resp.getBody());

            payment.setStatus(resp.getBody());
            paymentRepository.save(payment);
            if(payment.getStatus()==PaymentStatus.SUCCESSFUL) {
                ResponseEntity<String> response
                        = restTemplate.postForEntity( payment.getCheckStatusUrl()+ "/true", null, String.class);
            }else if(payment.getStatus()==PaymentStatus.FAILED || payment.getStatus()==PaymentStatus.ERROR){
                ResponseEntity<String> response
                        = restTemplate.postForEntity(payment.getCheckStatusUrl() + "/false", null, String.class);
            }
        });

    }

    @Override
    public void updateIntegratedSoftwareStatus() {
        LOGGER.info("Checking if payment is recorded on seller software...");
        List<Payment> payments = paymentRepository.findAllByStatusAndCheckedStatus(PaymentStatus.SUCCESSFUL,false);
        payments.parallelStream().forEach(payment -> {

            LOGGER.info("Founded payment: " + payment.getId() + " ,status: " + payment.getStatus());
            LOGGER.info("Getting status on url: " + payment.getCheckStatusUrl());
            ResponseEntity<Boolean> resp = restTemplate.getForEntity(payment.getCheckStatusUrl(),Boolean.class);
            if(resp.getBody()) {
                payment.setCheckedStatus(true);
                paymentRepository.save(payment);
            }else{
                LOGGER.info("Posting status on seller app on url: " + payment.getCheckStatusUrl());
                restTemplate.postForEntity(payment.getCheckStatusUrl()+"/true",null,String.class);
            }
        });

    }

    @Scheduled(fixedRate = 60000)
    public void changeStatus() {
        updateStatus();
    }

    @Scheduled(fixedRate = 60000)
    public void checkIntegratedSoftwareStatus() {
        updateIntegratedSoftwareStatus();
    }


}