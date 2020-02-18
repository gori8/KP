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
import rs.ac.uns.ftn.url.AmountAndUrlDTO;
import rs.ac.uns.ftn.url.UrlClass;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


@Service
public class InfoServiceImpl implements InfoService{

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    NacinPlacanjaRepository nacinPlacanjaRepository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    SellerRepository sellerRepository;

    @Autowired
    RedUrlRepository redUrlRepository;

    @Override
    public Item editCasopis(CasopisDTO casopisDTO) throws Exception {

    Item newItem = null;
    newItem = itemRepository.findOneByUuid(UUID.fromString(casopisDTO.getUuid()));

    NacinPlacanja np = null;

    if(newItem == null){
        newItem = new Item();
        newItem.setNaziv(casopisDTO.getNaziv());
        newItem.setUuid(UUID.randomUUID());
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



    Item item = itemRepository.save(newItem);

    RedUrl redUrl = new RedUrl();
    redUrl.setItem(item);
    redUrl.setUrl(casopisDTO.getRedirectUrl());
    item.getRedUrls().add(redUrl);
    redUrl = redUrlRepository.save(redUrl);

    item = itemRepository.save(item);

    return item;

    }

    @Override
    public Item updateCasopis(CasopisDTO casopisDTO) {

        Item updateItem = itemRepository.findOneById(casopisDTO.getId());

        updateItem.setNaziv(casopisDTO.getNaziv());
        updateItem.setAmount(casopisDTO.getAmount());

        return itemRepository.save(updateItem);

    }

    @Override
    public List<Item> getAllCasopisi() {
        return itemRepository.findAll();
    }

    @Override
    public List<NacinPlacanjaDTO> getNacinePlacanjaZaCasopis(String casopisId){
        UUID uuid = UUID.fromString(casopisId);
        RedUrl redUrl = redUrlRepository.findOneByUuid(uuid);
        Item item = redUrl.getItem();

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
        Item c = itemRepository.findOneByUuid(uuid);

        RedUrl redUrl = new RedUrl();
        redUrl.setItem(c);
        redUrl.setUrl(redirectUrlDTO.getRedirectUrl());
        c.getRedUrls().add(redUrl);
        redUrl = redUrlRepository.save(redUrl);

        itemRepository.save(c);

        UrlDTO url = new UrlDTO(UrlClass.FRONT_KP+"/"+redUrl.getUuid().toString());

        return url;
    }

    @Override
    public AmountAndUrlDTO getAmountAndUrl(String id) {
        UUID uuid = UUID.fromString(id);
        RedUrl redUrl = redUrlRepository.findOneByUuid(uuid);
        AmountAndUrlDTO dto=new AmountAndUrlDTO();
        dto.setAmount(redUrl.getItem().getAmount());
        dto.setRedirectUrl(redUrl.getUrl());
        dto.setSellerEmail(redUrl.getItem().getSeller().getEmail());
        dto.setItemUuid(redUrl.getItem().getUuid().toString());
        return dto;
    }

    @Override
    public AmountAndUrlDTO getAmountAndUrlSub(String id) {
        UUID uuid = UUID.fromString(id);
        Item item = itemRepository.findOneByUuid(uuid);
        AmountAndUrlDTO dto=new AmountAndUrlDTO();
        dto.setAmount(item.getAmount());
        dto.setRedirectUrl("stagod");
        dto.setSellerEmail(item.getSeller().getEmail());
        dto.setItemUuid(item.getUuid().toString());
        return dto;
    }

    @Override
    public void register(RegisterDTO dto) {
        Seller seller = new Seller();
        seller.setEmail(dto.getEmail());
        seller.setUrl(dto.getUrl());
        seller = sellerRepository.save(seller);
    }

    @Override
    public ReturnLinksDTO add(CreateLinksDTO dto){
       // ConverterAES converter = new ConverterAES();

        ReturnLinksDTO ret = new ReturnLinksDTO();

        Seller seller = sellerRepository.findOneByEmail(dto.getEmail());
        if(seller == null){
            ret.setRegisterUrl(UrlClass.USER_AND_PAYMENT_URL+"register");
            return ret;
            /*seller = new Seller();
            seller.setEmail(dto.getEmail());
            seller = sellerRepository.save(seller);*/
        }

        Item item = new Item();
        item.setNaziv(dto.getNaziv());
        item.setAmount(dto.getAmount());
        item.setUuid(UUID.randomUUID());
        item.setSeller(seller);
        seller.getItems().add(item);
        ret.setUuid(item.getUuid().toString());

        for (Long npId:dto.getNaciniPlacanja()) {
            NacinPlacanja np = nacinPlacanjaRepository.getOne(npId);
            item.getNacinPlacanjaList().add(np);
            np.getItemList().add(item);
            nacinPlacanjaRepository.save(np);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            CheckSellerDTO checkSellerDTO = new CheckSellerDTO();
            checkSellerDTO.setEmail(dto.getEmail());

            HttpEntity<CheckSellerDTO> entity = new HttpEntity<CheckSellerDTO>(checkSellerDTO, headers);

            ResponseEntity<Boolean> response =
                    restTemplate.postForEntity(np.getCheckUrl(),entity,Boolean.class);

            Boolean exist = response.getBody();
            if(exist==false){
                LinkDTO linkDTO = new LinkDTO();
                // String hash = converter.convertToDatabaseColumn(item.getUuid().toString());
                String hash = item.getUuid().toString();
                linkDTO.setLink(UrlClass.FRONT_KP+"/paymentform/"+npId+"/"+hash);
                linkDTO.setNacinPlacanjaId(npId);

                ret.getLinks().add(linkDTO);
            }
        }


        item = itemRepository.save(item);

        RedUrl redUrl = new RedUrl();
        redUrl.setItem(item);
        redUrl.setUrl(dto.getRedirectUrl()+item.getUuid());
        item.getRedUrls().add(redUrl);
        redUrl = redUrlRepository.save(redUrl);

        item = itemRepository.save(item);

        sellerRepository.save(seller);

        return ret;
    }

    @Override
    public String registrationCompleted(RegistrationCompletedDTO dto){

        Item item = itemRepository.findOneByUuid(UUID.fromString(dto.getUuid()));
        String url = item.getSeller().getUrl()+"/"+dto.getNacinPlacanjaId();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CreateLinksDTO> entity = new HttpEntity<CreateLinksDTO>(null, headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity(url,entity,String.class);

        return response.getBody();
    }

    @Override
    public Object getForm(Long nacinPlacanjId,String uuid){
        String url = nacinPlacanjaRepository.getOne(nacinPlacanjId).getFormUrl();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<JSONObject> response =
                restTemplate.getForEntity(url,JSONObject.class);


        JSONObject jsonObject = response.getBody();

        String email = itemRepository.findOneByUuid(UUID.fromString(uuid)).getSeller().getEmail();
        jsonObject.put("sellerEmail", email);

        return jsonObject;
    }
}
