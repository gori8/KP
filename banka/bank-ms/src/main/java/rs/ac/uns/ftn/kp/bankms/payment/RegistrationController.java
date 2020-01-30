package rs.ac.uns.ftn.kp.bankms.payment;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.kp.bankms.client.SellerService;
import rs.ac.uns.ftn.kp.bankms.dto.RegistrationDTO;
import rs.ac.uns.ftn.kp.bankms.model.Seller;
import rs.ac.uns.ftn.url.CheckSellerDTO;

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

}
