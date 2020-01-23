package rs.ac.uns.ftn.kp.bankms.payment;

import rs.ac.uns.ftn.kp.bankms.client.ClientRepository;
import rs.ac.uns.ftn.kp.bankms.client.ClientService;
import rs.ac.uns.ftn.kp.bankms.model.Client;
import rs.ac.uns.ftn.kp.bankms.model.Payment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rs.ac.uns.ftn.url.UrlClass;

import java.util.List;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    //private static final String PAYMENT_URL_F = "%s/card/%s";
    public static final String NOT_FOUND = "notFound";

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    @Autowired
    RestTemplate restTemplate;

    @Override
    public String handleKpRequest(PaymentRequest kpRequestDto) {


        String fooResourceUrl1
                = UrlClass.DOBAVI_CENU_URL_SA_PAYMENT_INFO + kpRequestDto.getCasopisUuid();

        ResponseEntity<AmountAndUrlDTO> resp
                = restTemplate.getForEntity(fooResourceUrl1, AmountAndUrlDTO.class);


        Payment payment = new Payment();
        Payment savedPayment;

        PaymentResponse response1 = new PaymentResponse();


        String fooResourceUrl
                = UrlClass.DOBAVI_REDIRECT_URL_SA_BANKE;

        ObjectMapper mapper = new ObjectMapper();

        MappingClass mc = new MappingClass();
        mc.setAmount(resp.getBody().getAmount());
        mc.setErrorUrl(resp.getBody().getRedirectUrl() + "/false");
        mc.setFailedUrl(resp.getBody().getRedirectUrl() + "/false");
        mc.setSuccessUrl(resp.getBody().getRedirectUrl() + "/true");
        Client cl = clientRepository.findByCasopisUuid(UUID.fromString(kpRequestDto.getCasopisUuid()));
        mc.setMerchantId(cl.getMerchantId());

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
        //System.out.println(response.getBody());

        JSONObject actualObj=null;
        String ret = "";

        try {
            actualObj = new JSONObject(response.getBody());
            ret = actualObj.getString("url");
            payment.setUrl(ret);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        payment.setCasopisUuid(UUID.fromString(kpRequestDto.getCasopisUuid()));
        payment.setAmount(resp.getBody().getAmount());
        payment.setPlaceno(false);

        savedPayment = paymentRepository.save(payment);

        System.out.println(UrlClass.FRONT_BANKE + ret);
        return UrlClass.FRONT_BANKE + ret;

    }


    @Override
    public String useCardData(CardDataDto cardDataDto, String url) {

        String fooResourceUrl
                = UrlClass.DOBAVI_REDIRECT_URL_SA_BANKE + "/" + url;

        ObjectMapper mapper = new ObjectMapper();
        MappingClassCard mc = new MappingClassCard();

        mc.setPan(cardDataDto.getPan());
        mc.setHolderName(cardDataDto.getHolderName());
        mc.setSecurityCode(cardDataDto.getSecurityCode());
        mc.setValidTo(cardDataDto.getValidTo());

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
        System.out.println(response.getBody());


        JSONObject actualObj=null;
        String ret = "";
        String ret1 = "";


        try {
            actualObj = new JSONObject(response.getBody());
            ret = actualObj.getString("url");
            ret1 = actualObj.getString("successful");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Payment payment = paymentRepository.findOneByUrl("banka/card/" + url);
        Payment savedPayment = new Payment();

        if(ret1.equals("true")) {
            payment.setPlaceno(true);
        } else {
            payment.setPlaceno(false);
        }

        savedPayment = paymentRepository.save(payment);


        String postResourceUrl = ret;

        System.out.println(ret);

        entity = new HttpEntity<String>("", headers);

        ResponseEntity<String> frontUrl
                = restTemplate.postForEntity(postResourceUrl,entity,String.class);

        return frontUrl.getBody();
    }


}