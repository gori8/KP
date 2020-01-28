package rs.ac.uns.ftn.kp.bankms.payment;


import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rs.ac.uns.ftn.kp.bankms.client.ClientRepository;
import rs.ac.uns.ftn.kp.bankms.dto.RegistrationDTO;
import rs.ac.uns.ftn.kp.bankms.model.Client;
import org.springframework.security.crypto.password.PasswordEncoder;
import rs.ac.uns.ftn.kp.bankms.model.KpSeller;
import rs.ac.uns.ftn.kp.bankms.seller.KpSellerRepository;
import rs.ac.uns.ftn.url.CheckSellerDTO;

import java.util.UUID;

@RestController
@RequestMapping("/api/bankms")
@CrossOrigin("*")
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    KpSellerRepository kpSellerRepository;

    public PaymentController(PaymentServiceImpl paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<String> paymentRequest(@RequestBody PaymentRequest kpRequestDto) {

        return new ResponseEntity<String>("\""+paymentService.handleKpRequest(kpRequestDto)+"\"", HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET,value = "/{id}/{status}")
    public ResponseEntity<String> setPaymentStatus(@PathVariable String id, @PathVariable String status) {
        return ResponseEntity.ok(paymentService.setPaymentStatus(id,status));
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<Long> register(@RequestBody RegistrationDTO registrationDTO){

        KpSeller seller = new KpSeller();
        seller.setEmail(registrationDTO.getSellerEmail());



        Client client = new Client();
        client.setSeller(seller);
        seller.getBankClients().add(client);
        client.setFirstName(registrationDTO.getFirstName());
        client.setLastName(registrationDTO.getLastName());
        client.setMerchantId(registrationDTO.getMerchantId());
        client.setMerchantPassword(passwordEncoder.encode(registrationDTO.getMerchantPassword()));
        Long ret=null;

        try{
            ret = clientRepository.save(client).getId();
            kpSellerRepository.save(seller);
        }catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<Long>(ret, HttpStatus.OK);
    }

    @RequestMapping(value = "/check/email", method = RequestMethod.POST)
    public ResponseEntity checkEmail(@RequestBody CheckSellerDTO checkSellerDTO){
        if(kpSellerRepository.findOneByEmail(checkSellerDTO.getEmail())==null){
            return ResponseEntity.ok(false);
        }else{
            return ResponseEntity.ok(true);
        }
    }

}