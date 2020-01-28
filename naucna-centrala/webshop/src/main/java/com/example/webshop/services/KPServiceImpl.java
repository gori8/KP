package com.example.webshop.services;

import com.example.webshop.dto.*;
import com.example.webshop.model.Casopis;
import com.example.webshop.model.Izdanje;
import com.example.webshop.model.Link;
import com.example.webshop.model.NacinPlacanja;
import com.example.webshop.repository.CasopisRepository;
import com.example.webshop.repository.IzdanjeRepository;
import com.example.webshop.repository.LinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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


    @Override
    public void createLinks(Long casopisId){
        Casopis casopis = casopisRepository.getOne(casopisId);

        ItemDTO dto = new ItemDTO();
        dto.setNaziv(casopis.getNaziv());
        dto.setAmount(casopis.getClanarina());
        dto.setRedirectUrl(UrlClass.REDIRECT_URL_REGISTRATION);
        dto.setNaciniPlacanja(new ArrayList<>());
        dto.setEmail(casopis.getGlavniUrednik().getEmail());
        for (NacinPlacanja np:casopis.getNaciniPlacanja()) {
            dto.getNaciniPlacanja().add(np.getId());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ItemDTO> entity = new HttpEntity<ItemDTO>(dto, headers);

        ResponseEntity<ReturnLinksDTO> response =
                restTemplate.postForEntity(UrlClass.USER_AND_PAYMENT_URL+"register",entity,ReturnLinksDTO.class);

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
}
