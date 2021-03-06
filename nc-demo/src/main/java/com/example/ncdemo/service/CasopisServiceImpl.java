package com.example.ncdemo.service;

import com.example.ncdemo.dto.CasopisDTO;
import com.example.ncdemo.model.Casopis;
import com.example.ncdemo.dto.MappingClass;
import com.example.ncdemo.repository.CasopisRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rs.ac.uns.ftn.url.UrlClass;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CasopisServiceImpl implements CasopisService {

    //private static final String redirectUrl = "https://localhost:9000/nc/casopis/";
    //private static final String fooResourceUrl = "https://localhost:8771/userandpayment/api/url";
    //private static final String ncFront = "https://localhost:4500/casopis/";



    @Autowired
    CasopisRepository casopisRepository;

    @Override
    public CasopisDTO getCasopis(String id){
        UUID uuid = UUID.fromString(id);
        Casopis c = casopisRepository.findOneByUuid(uuid);
        CasopisDTO cDTO = new CasopisDTO(c.getUuid().toString(),c.getNaziv(),c.getPlacen());

        return cDTO;
    }

    @Override
    public List<CasopisDTO> getAllCasopis(){
        List<Casopis> cList = casopisRepository.findAll();

        List<CasopisDTO> cListDTO = new ArrayList<>();

        for (Casopis c: cList) {
            System.out.println(c.getUuid().toString());
            CasopisDTO cDTO = new CasopisDTO(c.getUuid().toString(),c.getNaziv(),c.getPlacen());
            cListDTO.add(cDTO);
        }

        return cListDTO;
    }

    @Autowired
    RestTemplate restTemplate;


    @Override
    public String getRedirectUrl(String uapId){
        ObjectMapper mapper = new ObjectMapper();

        MappingClass mc = new MappingClass();
        mc.setRedirectUrl(UrlClass.DOBAVI_CASOPIS_NA_NC_DEMO+uapId);
        mc.setId(uapId);

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
                = restTemplate.postForEntity(UrlClass.DOBAVI_KP_FRONT_URL_SA_NACINIMA_PLACANJA_FROM_PAYMENT_INFO,entity,String.class);

        JSONObject actualObj=null;
        String ret = "";

        try {
            actualObj = new JSONObject(response.getBody());
            ret = actualObj.getString("url");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ret;
    }

    @Override
    public String changePayed(String uuid, Boolean success){
        if(success) {
            UUID realUuid = UUID.fromString(uuid);
            Casopis casopis = casopisRepository.findOneByUuid(realUuid);

            if (casopis == null) {
                return UrlClass.FRONT_NC + uuid;
            }

            casopis.setPlacen(true);
            casopisRepository.save(casopis);

            return UrlClass.FRONT_NC + uuid;
        }else{
            return UrlClass.FRONT_NC + uuid;
        }
    }
}
