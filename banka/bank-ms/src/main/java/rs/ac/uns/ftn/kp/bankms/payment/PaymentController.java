package rs.ac.uns.ftn.kp.bankms.payment;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rs.ac.uns.ftn.kp.bankms.client.SellerService;
import rs.ac.uns.ftn.kp.bankms.dto.RegistrationDTO;
import rs.ac.uns.ftn.kp.bankms.model.Seller;
import rs.ac.uns.ftn.url.CheckSellerDTO;

@RestController
@RequestMapping("/api/bankms")
@CrossOrigin("*")
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @Autowired
    SellerService sellerService;

    @Autowired
    PasswordEncoder passwordEncoder;


    @PostMapping
    public ResponseEntity<String> paymentRequest(@RequestBody PaymentRequest kpRequestDto) {

        return new ResponseEntity<String>("\""+paymentService.handleKpRequest(kpRequestDto)+"\"", HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET,value = "/{id}/{status}")
    public ResponseEntity<String> setPaymentStatus(@PathVariable String id, @PathVariable String status) {
        return ResponseEntity.ok(paymentService.setPaymentStatus(id,status));
    }



}