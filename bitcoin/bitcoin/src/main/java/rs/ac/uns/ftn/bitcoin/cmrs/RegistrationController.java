package rs.ac.uns.ftn.bitcoin.cmrs;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.bitcoin.dto.RegistrationDTO;
import rs.ac.uns.ftn.url.CheckSellerDTO;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@RestController
@RequestMapping("/api/register")
@CrossOrigin(origins = "*")
public class RegistrationController {

    private final Logger LOGGER = LoggerFactory.getLogger(RegistrationController.class);

    @Autowired
    SellerRepository sellerRepository;

    @RequestMapping( method = RequestMethod.POST)
    public ResponseEntity<Long> register(@RequestBody RegistrationDTO registrationDTO) {
        Seller seller = new Seller();
        seller.setToken(registrationDTO.getToken());
        seller.setSellerEmail(registrationDTO.getSellerEmail());
        Long ret = sellerRepository.save(seller).getId();

        LOGGER.info(LocalDateTime.now() + "     Registering seller    " + "mail:  " + seller.getSellerEmail() + "   token:   " + seller.getToken() +  "  on bitcoin microservice");
        LOGGER.info(LocalDateTime.now() + "     Seller with mail:   " + seller.getSellerEmail() + " is successfully registered on bitcoin microservice.");

        return new ResponseEntity<Long>(ret, HttpStatus.OK);
    }


    @RequestMapping(value = "/check/mail", method = RequestMethod.POST)
    public ResponseEntity checkEmail(@RequestBody CheckSellerDTO checkSellerDTO){
        if(sellerRepository.findBySellerEmail(checkSellerDTO.getEmail())==null){
            return ResponseEntity.ok(false);
        }else{
            return ResponseEntity.ok(true);
        }
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public ResponseEntity getForm() throws IOException, ParseException {
        JSONParser parser = new JSONParser();


        Object obj = parser.parse(new FileReader("bitcoin/bitcoin/src/main/resources/json/form.json"));
        JSONObject jsonObject = (JSONObject) obj;

        String name = (String)jsonObject.get("image");
        if(name!=null){
            File file = new File("bitcoin/bitcoin/src/main/resources/json/images/"+name);
            String encodeImage = Base64.getEncoder().withoutPadding().encodeToString(Files.readAllBytes(file.toPath()));

            jsonObject.put("encodeImage", encodeImage);
        }

        return new ResponseEntity(jsonObject, HttpStatus.OK);

    }

}
