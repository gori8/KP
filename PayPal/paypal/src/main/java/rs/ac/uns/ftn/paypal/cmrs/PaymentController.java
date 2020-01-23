package rs.ac.uns.ftn.paypal.cmrs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.paypal.dto.CreatePaymentRequest;
import rs.ac.uns.ftn.paypal.dto.CreatePaymentResponse;
import rs.ac.uns.ftn.paypal.dto.ExecutePaymentRequest;
import rs.ac.uns.ftn.paypal.dto.RegistrationDTO;
import rs.ac.uns.ftn.url.UrlClass;

import java.util.UUID;

@RequestMapping("/api/paypal")
@RestController
@CrossOrigin("*")
public class PaymentController {

    //private static final String FRONTAPP_URL="http://localhost:4400/";

    @Autowired
    PaymentService paymentService;

    @Autowired
    SellerRepository sellerRepository;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<Long> register(@RequestBody RegistrationDTO registrationDTO) {

        Seller seller = new Seller();
        seller.setCasopisID(UUID.fromString(registrationDTO.getUuid()));
        seller.setEmail(registrationDTO.getEmail());
        seller.setMerchant_id(registrationDTO.getMerchantId());
        Long ret = sellerRepository.save(seller).getId();

        return new ResponseEntity<Long>(ret, HttpStatus.OK);
    }

    @RequestMapping(value = "/getUrl", method = RequestMethod.POST)
    public String getUrl(@RequestBody CreatePaymentRequest id)  {
        return "\""+ UrlClass.FRONT_PAYPAL+id.getCasopisUuid()+"\"";
    }

    @PostMapping
    public CreatePaymentResponse create(@RequestBody CreatePaymentRequest request) {
        System.out.println(request);
        return paymentService.createPayment(request);
    }

    @RequestMapping(value = "/execute", method = RequestMethod.POST)
    public ResponseEntity<String> create(@RequestBody ExecutePaymentRequest request) {
        System.out.println(request.getPayerID());
        return new ResponseEntity<String>(paymentService.executePayment(request), HttpStatus.OK);
    }

    @GetMapping("/cancel/{id}")
    public String cancelPayment(@PathVariable("id") Long id)  {
        String redirectUrl = paymentService.cancelPayment(id);
        return redirectUrl;
    }


}
