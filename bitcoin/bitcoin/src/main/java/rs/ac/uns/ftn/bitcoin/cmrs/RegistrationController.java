package rs.ac.uns.ftn.bitcoin.cmrs;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.bitcoin.dto.RegistrationDTO;
import rs.ac.uns.ftn.url.CheckSellerDTO;

import java.util.UUID;

@RestController
@RequestMapping("/api/register")
@CrossOrigin(origins = "*")
public class RegistrationController {

    @Autowired
    SellerRepository sellerRepository;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<Long> register(@RequestBody RegistrationDTO registrationDTO) {
        Seller seller = new Seller();
        seller.setToken(registrationDTO.getToken());
        seller.setSellerEmail(registrationDTO.getSellerEmail());
        Long ret = sellerRepository.save(seller).getId();

        return new ResponseEntity<Long>(ret, HttpStatus.OK);
    }


    @RequestMapping(value = "/check/email", method = RequestMethod.POST)
    public ResponseEntity checkEmail(@RequestBody CheckSellerDTO checkSellerDTO){
        if(sellerRepository.findBySellerEmail(checkSellerDTO.getEmail())==null){
            return ResponseEntity.ok(false);
        }else{
            return ResponseEntity.ok(true);
        }
    }

}