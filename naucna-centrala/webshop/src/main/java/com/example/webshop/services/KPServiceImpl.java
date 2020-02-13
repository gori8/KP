package com.example.webshop.services;

import com.example.webshop.dto.*;
import com.example.webshop.model.*;
import com.example.webshop.repository.CasopisRepository;
import com.example.webshop.repository.IzdanjeRepository;
import com.example.webshop.repository.KorisnikRepository;
import com.example.webshop.repository.LinkRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rs.ac.uns.ftn.url.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

@Service
public class KPServiceImpl implements KPService {

    @Autowired
    CasopisRepository casopisRepository;

    @Autowired
    LinkRepository linkRepository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    IzdanjeRepository izdanjeRepository;

    @Autowired
    KorisnikRepository korisnikRepository;

    @Override
    public void createLinks(Long casopisId) throws Exception {
        Casopis casopis = casopisRepository.getOne(casopisId);

        ItemDTO dto = new ItemDTO();
        dto.setNaziv(casopis.getNaziv()+"");
        dto.setRedirectUrl(UrlClass.REDIRECT_URL_REGISTRATION);
        dto.setNaciniPlacanja(new ArrayList<>());
        dto.setEmail(casopis.getGlavniUrednik().getEmail());
        for (NacinPlacanja np:casopis.getNaciniPlacanja()) {
            dto.getNaciniPlacanja().add(np.getId());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ItemDTO> entity = new HttpEntity<ItemDTO>(dto, headers);
        ResponseEntity<ReturnLinksDTO> response=null;

        try {
             response=
                    restTemplate.postForEntity(UrlClass.USER_AND_PAYMENT_URL + "add", entity, ReturnLinksDTO.class);

        }catch (Exception e) {
            if(response.getStatusCode()== HttpStatus.valueOf(401)){
                System.out.println("User is not registered");
                System.out.println("Trying to register user...");
                ReturnLinksDTO responseBody = response.getBody();
                HttpHeaders headersReg = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<ItemDTO> entityReg = new HttpEntity<ItemDTO>(dto, headersReg);
                ResponseEntity<Boolean> responseReg = restTemplate.postForEntity(response.getBody().getRegisterUrl(),entityReg,Boolean.class);
                if(responseReg.getBody()){
                    System.out.println("User registered successfully");
                    response=
                            restTemplate.postForEntity(UrlClass.USER_AND_PAYMENT_URL + "add", entity, ReturnLinksDTO.class);
                }else{
                    throw new Exception();
                }
            }
        }
        ReturnLinksDTO responseBody = response.getBody();

        casopis.setUuid(UUID.fromString(responseBody.getUuid()));

        if(response.getStatusCode().value()==200) {
            for (LinkDTO linkDTO : responseBody.getLinks()) {
                Link link = new Link();
                link.setCasopis(casopis);
                link.setUrl(linkDTO.getLink());
                link.setCompleted(false);
                link.setNacinPlacanja(linkDTO.getNacinPlacanjaId());
                link = linkRepository.save(link);

                casopis.getLinkovi().add(link);
            }
        }

        casopisRepository.save(casopis);
    }

    public String completePayment(String uuid, Long nacinPlacanjaId){
        Link link = linkRepository.findOneByCasopisUuidAndNacinPlacanja(UUID.fromString(uuid),nacinPlacanjaId);
        link.setCompleted(true);
        linkRepository.save(link);

        return UrlClass.FRON_WEBSHOP+"tasks";
    }

    public Long addNewNumber(IzdanjeDTO dto){
        Izdanje izdanje = new Izdanje();
        Casopis casopis = casopisRepository.getOne(dto.getCasopisId());

        izdanje.setBroj(dto.getBroj());
        izdanje.setCena(dto.getCena());
        izdanje.setNaziv(dto.getNaziv());
        izdanje.setCasopis(casopis);
        casopis.getIzdanja().add(izdanje);

        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setAmount(dto.getCena());
        itemDTO.setEmail(izdanje.getCasopis().getGlavniUrednik().getEmail());
        itemDTO.setNaciniPlacanja(new ArrayList<>());
        itemDTO.setNaziv(dto.getNaziv());
        for (NacinPlacanja np:izdanje.getCasopis().getNaciniPlacanja()) {
            itemDTO.getNaciniPlacanja().add(np.getId());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ItemDTO> entity = new HttpEntity<ItemDTO>(itemDTO, headers);

        ResponseEntity<ReturnLinksDTO> response =
                restTemplate.postForEntity(UrlClass.USER_AND_PAYMENT_URL+"register",entity,ReturnLinksDTO.class);

        ReturnLinksDTO responseBody = response.getBody();

        izdanje.setDatumIzdanja(new Date());
        izdanje.setUuid(UUID.fromString(responseBody.getUuid()));

        izdanje = izdanjeRepository.save(izdanje);
        casopisRepository.save(casopis);

        return izdanje.getId();
    }

    public String getRedirectUrl(String uapId,String username){
        ObjectMapper mapper = new ObjectMapper();

        MappingClass mc = new MappingClass();
        mc.setRedirectUrl(UrlClass.REDIRECT_URL_PAYMENT+username+"/"+uapId);
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
    public String changePayed(String uuid, Boolean success, String username) {
        if (success) {
            UUID realUuid = UUID.fromString(uuid);
            Izdanje izdanje = izdanjeRepository.findOneByUuid(realUuid);

            if(izdanje==null){
                return UrlClass.FRON_WEBSHOP+"paymentresponse/failed";
            }

            Korisnik korisnik = korisnikRepository.findOneByUsername(username);

            izdanje.getKupci().add(korisnik);
            izdanje = izdanjeRepository.save(izdanje);

            korisnik.getCasopisiKupci().add(izdanje);
            korisnik = korisnikRepository.save(korisnik);

            return UrlClass.FRON_WEBSHOP+"paymentresponse/success";
        } else {
            return UrlClass.FRON_WEBSHOP+"paymentresponse/failed";
        }
    }
}
