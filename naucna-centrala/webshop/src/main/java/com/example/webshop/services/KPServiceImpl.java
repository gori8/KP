package com.example.webshop.services;

import com.example.webshop.dto.CreateLinksDTO;
import com.example.webshop.dto.LinkDTO;
import com.example.webshop.dto.MappingClass;
import com.example.webshop.dto.ReturnLinksDTO;
import com.example.webshop.model.Casopis;
import com.example.webshop.model.Link;
import com.example.webshop.model.NacinPlacanja;
import com.example.webshop.repository.CasopisRepository;
import com.example.webshop.repository.LinkRepository;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rs.ac.uns.ftn.url.*;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class KPServiceImpl implements KPService {

    public static final String userAndPaymentUrl = "http://localhost:8090/api";

    @Autowired
    CasopisRepository casopisRepository;

    @Autowired
    LinkRepository linkRepository;

    @Override
    public void createLinks(Long casopisId){
        Casopis casopis = casopisRepository.getOne(casopisId);

        CreateLinksDTO dto = new CreateLinksDTO();
        dto.setNaziv(casopis.getNaziv());
        dto.setAmount(casopis.getClanarina());
        dto.setRedirectUrl("http://localhost:9000/nc/payment/complete/");
        dto.setNaciniPlacanja(new ArrayList<>());
        for (NacinPlacanja np:casopis.getNaciniPlacanja()) {
            dto.getNaciniPlacanja().add(np.getId());
        }

        RestTemplate restTemplate=null;
        try {
            restTemplate = restTemplate();
        }catch (Exception e){
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CreateLinksDTO> entity = new HttpEntity<CreateLinksDTO>(dto, headers);

        if(restTemplate == null){
            System.out.println("restTemplate je null");
        }
        if(entity == null){
            System.out.println("entity je null");
        }
        if(userAndPaymentUrl == null){
            System.out.println("userAndPaymentUrl je null");
        }

        ResponseEntity<ReturnLinksDTO> response =
                restTemplate.postForEntity(userAndPaymentUrl+"/links",entity,ReturnLinksDTO.class);

        ReturnLinksDTO responseBody = response.getBody();

        casopis.setUuid(UUID.fromString(responseBody.getUuid()));
        for (LinkDTO linkDTO:responseBody.getLinks()) {
            Link link = new Link();
            link.setCasopis(casopis);
            link.setUrl(linkDTO.getLink());
            link.setCompleted(false);
            link.setNacinPlacanja(linkDTO.getNacinPlacanjaId());
            link = linkRepository.save(link);

            casopis.getLinkovi().add(link);
        }

        casopisRepository.save(casopis);
    }

    public RestTemplate restTemplate() throws Exception{
        KeyStore clientStore = KeyStore.getInstance("JKS");
        clientStore.load(new FileInputStream("src/main/resources/identity.jks"), "secret".toCharArray());
        KeyStore trustStore = KeyStore.getInstance("JKS");
        trustStore.load(new FileInputStream("src/main/resources/truststore.jks"), "secret".toCharArray());

        SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
        sslContextBuilder.setProtocol("TLS");
        sslContextBuilder.loadKeyMaterial(clientStore, "secret".toCharArray());
        sslContextBuilder.loadTrustMaterial(trustStore,null);

        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContextBuilder.build());
        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(sslConnectionSocketFactory)
                .build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        requestFactory.setConnectTimeout(10000); // 10 seconds
        requestFactory.setReadTimeout(10000); // 10 seconds
        return new RestTemplate(requestFactory);
    }
}
