package rs.ac.uns.ftn.paypal.cmrs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.paypal.dto.RegistrationDTO;
import rs.ac.uns.ftn.url.CheckSellerDTO;

import java.util.UUID;

@RequestMapping("/api/register")
@RestController
@CrossOrigin("*")
public class RegistrationController {

    @Autowired
    SellerRepository sellerRepository;

    @RequestMapping(value = "/check/email", method = RequestMethod.POST)
    public ResponseEntity checkEmail(@RequestBody CheckSellerDTO checkSellerDTO){
        if(sellerRepository.findBySellerEmail(checkSellerDTO.getEmail())==null){
            return ResponseEntity.ok(false);
        }else{
            return ResponseEntity.ok(true);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Long> register(@RequestBody RegistrationDTO registrationDTO) {

        Seller seller = new Seller();
        seller.setPaypalEmail(registrationDTO.getEmail());
        seller.setMerchant_id(registrationDTO.getMerchantId());
        seller.setSellerEmail(registrationDTO.getSellerEmail());
        Long ret = sellerRepository.save(seller).getId();

        return new ResponseEntity<Long>(ret, HttpStatus.OK);
    }

}
