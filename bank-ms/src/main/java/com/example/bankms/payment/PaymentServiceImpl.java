package com.example.bankms.payment;

import com.example.bankms.client.ClientRepository;
import com.example.bankms.client.ClientService;
import com.example.bankms.model.Client;
import com.example.bankms.model.Payment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
    public PaymentResponse handleKpRequest(PaymentRequest kpRequestDto) {

        Payment payment = new Payment();
        Payment savedPayment;

        PaymentResponse response1 = new PaymentResponse();


        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl
                = "http://localhost:8091/payment";

        ObjectMapper mapper = new ObjectMapper();

        MappingClass mc = new MappingClass();
        mc.setAmount(kpRequestDto.getAmount());
        mc.setErrorUrl("");
        mc.setFailedUrl("");
        mc.setSuccessUrl("");
        Client cl = clientRepository.findByCasopisId(kpRequestDto.getCasopisId());
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
        System.out.println(response.getBody());



        return null;
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


        return null;
    }


}
