package rs.ac.uns.ftn.kp.bankms.payment;


import com.netflix.ribbon.proxy.annotation.Http;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.kp.bankms.client.SellerService;
import rs.ac.uns.ftn.kp.bankms.dto.RegistrationDTO;
import rs.ac.uns.ftn.kp.bankms.model.Seller;
import rs.ac.uns.ftn.url.CheckSellerDTO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.UUID;

@RestController
@RequestMapping("/api/register")
@CrossOrigin("*")
public class RegistrationController {

    @Autowired
    SellerService sellerService;


    @RequestMapping( method = RequestMethod.POST)
    public ResponseEntity<Long> register(@RequestBody RegistrationDTO registrationDTO){

        Seller seller = new Seller();
        seller.setSeller(registrationDTO.getSellerEmail());
        seller.setFirstName(registrationDTO.getFirstName());
        seller.setLastName(registrationDTO.getLastName());
        seller.setMerchantId(registrationDTO.getMerchantId());
        seller.setMerchantPassword(registrationDTO.getMerchantPassword());
        Long ret=null;

        try{
            seller = sellerService.save(seller);
            ret = seller.getId();
        }catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<Long>(ret, HttpStatus.OK);
    }

    @RequestMapping(value = "/check/mail", method = RequestMethod.POST)
    public ResponseEntity checkEmail(@RequestBody CheckSellerDTO checkSellerDTO){
        if(sellerService.getBySeller(checkSellerDTO.getEmail())==null){
            return ResponseEntity.ok(false);
        }else{
            return ResponseEntity.ok(true);
        }
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public ResponseEntity getForm() throws IOException, ParseException {
        JSONParser parser = new JSONParser();


        Object obj = parser.parse(new FileReader("banka/bank-ms/src/main/resources/json/form.json"));
        JSONObject jsonObject = (JSONObject) obj;

        String name = (String)jsonObject.get("image");
        if(name!=null){
            File file = new File("banka/bank-ms/src/main/resources/json/images/"+name);
            String encodeImage = Base64.getEncoder().withoutPadding().encodeToString(Files.readAllBytes(file.toPath()));

            jsonObject.put("encodeImage", encodeImage);
        }

        return new ResponseEntity(jsonObject, HttpStatus.OK);

    }

}
