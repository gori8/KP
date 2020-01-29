package com.example.userandpaymentinfo.service;

import com.example.userandpaymentinfo.dto.*;
import com.example.userandpaymentinfo.model.*;
import com.example.userandpaymentinfo.repository.*;
import com.netflix.discovery.converters.Auto;
import org.hibernate.annotations.Check;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
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
    public ReturnLinksDTO register(CreateLinksDTO dto){
       // ConverterAES converter = new ConverterAES();

        ReturnLinksDTO ret = new ReturnLinksDTO();

        Seller seller = sellerRepository.findOneByEmail(dto.getEmail());
        if(seller == null){
            seller = new Seller();
            seller.setEmail(dto.getEmail());
            seller = sellerRepository.save(seller);
        }

        Item item = new Item();
        item.setNaziv(dto.getNaziv());
        item.setAmount(dto.getAmount());
        item.setUuid(UUID.randomUUID());
        item.setRedirectUrl(dto.getRedirectUrl()+item.getUuid());
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
        sellerRepository.save(seller);

        return ret;
    }

    @Override
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

    @Override
    public Object getForm(String folder,String uuid){
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader("userAndPaymentInfo/src/main/resources/json/"+folder+"/form.json"));
            JSONObject jsonObject = (JSONObject) obj;

            String email = itemRepository.findOneByUuid(UUID.fromString(uuid)).getSeller().getEmail();
            jsonObject.put("sellerEmail", email);

            return jsonObject;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Map<String,String> getImage(String folder,String name){
        try {
            File file = new File("userAndPaymentInfo/src/main/resources/json/"+folder+"/images/"+name);
            String encodeImage = Base64.getEncoder().withoutPadding().encodeToString(Files.readAllBytes(file.toPath()));
            Map<String, String> jsonMap = new HashMap<>();
            jsonMap.put("content", encodeImage);

            return jsonMap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
