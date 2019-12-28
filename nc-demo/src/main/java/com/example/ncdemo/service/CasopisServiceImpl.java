package com.example.ncdemo.service;

import com.example.ncdemo.dto.CasopisDTO;
import com.example.ncdemo.model.Casopis;
import com.example.ncdemo.dto.MappingClass;
import com.example.ncdemo.repository.CasopisRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CasopisServiceImpl implements CasopisService {

    private static final String redirectUrl = "http://localhost:9000/nc/casopis/";
    private static final String fooResourceUrl = "http://localhost:8090/api/url";
    private static final String ncFront = "http://localhost:4500/casopis/";

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

    @Override
    public String getRedirectUrl(String uapId){
        RestTemplate restTemplate = new RestTemplate();

        ObjectMapper mapper = new ObjectMapper();

        MappingClass mc = new MappingClass();
        mc.setRedirectUrl(redirectUrl+uapId);
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
                = restTemplate.postForEntity(fooResourceUrl,entity,String.class);

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
                return ncFront + uuid;
            }

            casopis.setPlacen(true);
            casopisRepository.save(casopis);

            return ncFront + uuid;
        }else{
            return ncFront + uuid;
        }
    }
}
