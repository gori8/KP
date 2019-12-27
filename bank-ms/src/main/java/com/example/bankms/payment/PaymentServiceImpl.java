package com.example.bankms.payment;

import com.example.bankms.client.ClientRepository;
import com.example.bankms.client.ClientService;
import com.example.bankms.model.Client;
import com.example.bankms.model.Payment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class PaymentServiceImpl implements PaymentService {

    private static final String PAYMENT_URL_F = "%s/card/%s";
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

    @Override
    public String handleKpRequest(PaymentRequest kpRequestDto) {

        Payment payment = new Payment();
        Payment savedPayment;

        PaymentResponse response1 = new PaymentResponse();


        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl
                = "http://localhost:8091/payment";

        ObjectMapper mapper = new ObjectMapper();

        MappingClass mc = new MappingClass();
        mc.setAmount(kpRequestDto.getAmount());
        mc.setErrorUrl("https://www.facebook.com/");
        mc.setFailedUrl("https://github.com/");
        mc.setSuccessUrl("https://www.youtube.com/");
        Client cl = clientRepository.findByCasopisUuid(kpRequestDto.getCasopisUuid());
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

        payment.setCasopisUuid(kpRequestDto.getCasopisUuid());
        payment.setAmount(kpRequestDto.getAmount());
        payment.setPlaceno(false);

        savedPayment = paymentRepository.save(payment);

        return "http://localhost:4300/" + ret;

    }

    @Override
    public String useCardData(CardDataDto cardDataDto, String url) {

        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl
                = "http://localhost:8091/payment/" + url;

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
            ret1 = actualObj.getString("placeno");
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

        return ret;
    }


}