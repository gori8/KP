package com.example.userandpaymentinfo.service;

import com.example.userandpaymentinfo.dto.*;
import com.example.userandpaymentinfo.model.*;
import com.example.userandpaymentinfo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rs.ac.uns.ftn.url.UrlClass;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
public class InfoServiceImpl implements InfoService{

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    NacinPlacanjaRepository nacinPlacanjaRepository;

    @Autowired
    RestTemplate restTemplate;

    @Override
    public Item editCasopis(CasopisDTO casopisDTO) throws Exception {

    Item newItem = null;
    newItem = itemRepository.findOneByUuid(UUID.fromString(casopisDTO.getUuid()));

    NacinPlacanja np = null;

    if(newItem == null){
        newItem = new Item();
        newItem.setNaziv(casopisDTO.getNaziv());
        newItem.setUuid(UUID.randomUUID());
        newItem.setRedirectUrl(casopisDTO.getRedirectUrl());
        newItem.setAmount(casopisDTO.getAmount());
    }
    else{
        np = nacinPlacanjaRepository.findByIdIfInCasopis(casopisDTO.getNacinPlacanjaId(), newItem.getId());
    }

    if(np == null){
        np = nacinPlacanjaRepository.getOne(casopisDTO.getNacinPlacanjaId());
        newItem.getNacinPlacanjaList().add(np);
        np.getItemList().add(newItem);

        nacinPlacanjaRepository.save(np);
    }else{
        throw new Exception();
    }

    return itemRepository.save(newItem);

    }

    @Override
    public Item updateCasopis(CasopisDTO casopisDTO) {

        Item updateItem = itemRepository.findOneById(casopisDTO.getId());

        updateItem.setNaziv(casopisDTO.getNaziv());
        updateItem.setAmount(casopisDTO.getAmount());
        updateItem.setRedirectUrl(casopisDTO.getRedirectUrl());

        return itemRepository.save(updateItem);

    }

    @Override
    public List<Item> getAllCasopisi() {
        return itemRepository.findAll();
    }

    @Override
    public List<NacinPlacanjaDTO> getNacinePlacanjaZaCasopis(String casopisId){
        UUID uuid = UUID.fromString(casopisId);
        Item item = itemRepository.findOneByUuid(uuid);

        List<NacinPlacanjaDTO> ret = new ArrayList<NacinPlacanjaDTO>();

        for (NacinPlacanja np: item.getNacinPlacanjaList()) {
            NacinPlacanjaDTO npDTO = new NacinPlacanjaDTO(np.getId(),np.getNacinPlacanja(),np.getUrl());
            ret.add(npDTO);
        }

        return ret;
    }

    @Override
    public UrlDTO getUrl(RedirectUrlDTO redirectUrlDTO){
        UUID uuid = UUID.fromString(redirectUrlDTO.getId());
        System.out.println(redirectUrlDTO.getId());
        Item c = itemRepository.findOneByUuid(uuid);
        System.out.println(redirectUrlDTO.getId());
        c.setRedirectUrl(redirectUrlDTO.getRedirectUrl());

        itemRepository.save(c);

        UrlDTO url = new UrlDTO(UrlClass.FRONT_KP+"/"+uuid.toString());

        return url;
    }

    @Override
    public AmountAndUrlDTO getAmountAndUrl(String id) {
        UUID uuid = UUID.fromString(id);
        Item item = itemRepository.findOneByUuid(uuid);
        AmountAndUrlDTO dto=new AmountAndUrlDTO();
        dto.setAmount(item.getAmount());
        dto.setRedirectUrl(item.getRedirectUrl());
        return dto;
    }

    @Override
    public ReturnLinksDTO createLinks(CreateLinksDTO dto){
       // ConverterAES converter = new ConverterAES();

        ReturnLinksDTO ret = new ReturnLinksDTO();

        Item item = new Item();
        item.setNaziv(dto.getNaziv());
        item.setAmount(dto.getAmount());
        item.setUuid(UUID.randomUUID());
        item.setRedirectUrl(dto.getRedirectUrl()+item.getUuid());
        ret.setUuid(item.getUuid().toString());

        for (Long npId:dto.getNaciniPlacanja()) {
            NacinPlacanja np = nacinPlacanjaRepository.getOne(npId);
            item.getNacinPlacanjaList().add(np);
            np.getItemList().add(item);
            nacinPlacanjaRepository.save(np);

            LinkDTO linkDTO = new LinkDTO();
           // String hash = converter.convertToDatabaseColumn(item.getUuid().toString());
            String hash = item.getUuid().toString();
            linkDTO.setLink(UrlClass.FRONT_KP+"/paymentform/"+npId+"/"+hash);
            linkDTO.setNacinPlacanjaId(npId);

            ret.getLinks().add(linkDTO);
        }

        item = itemRepository.save(item);

        return ret;
    }

    public String registrationCompleted(RegistrationCompletedDTO dto){

        Item item = itemRepository.findOneByUuid(UUID.fromString(dto.getUuid()));
        String url = item.getRedirectUrl()+"/"+dto.getNacinPlacanjaId();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CreateLinksDTO> entity = new HttpEntity<CreateLinksDTO>(null, headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity(url,entity,String.class);

        return response.getBody();
    }
}
