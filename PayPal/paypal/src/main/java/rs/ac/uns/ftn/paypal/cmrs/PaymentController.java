package rs.ac.uns.ftn.paypal.cmrs;

import com.paypal.api.payments.Plan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.paypal.dto.CreatePaymentOrSubRequest;
import rs.ac.uns.ftn.paypal.dto.CreatePaymentOrSubResponse;
import rs.ac.uns.ftn.paypal.dto.ExecutePaymentRequest;
import rs.ac.uns.ftn.paypal.dto.RegistrationDTO;
import rs.ac.uns.ftn.url.UrlClass;

import java.net.URI;
import java.net.URISyntaxException;
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
    public String getUrl(@RequestBody CreatePaymentOrSubRequest id)  {
        return "\""+ UrlClass.FRONT_PAYPAL+id.getCasopisUuid()+"\"";
    }

    @PostMapping
    public CreatePaymentOrSubResponse create(@RequestBody CreatePaymentOrSubRequest request) {
        System.out.println(request);
        return paymentService.createPayment(request);
    }

    @RequestMapping(value = "/execute", method = RequestMethod.GET)
    public ResponseEntity create(@RequestParam("paymentId")String paymentId,@RequestParam("token")String token,@RequestParam("PayerID")String payerId ) throws URISyntaxException {
        ExecutePaymentRequest request = new ExecutePaymentRequest(paymentId,payerId,token);
        URI redirectUrl = new URI(paymentService.executePayment(request));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(redirectUrl);
        return ResponseEntity.status(HttpStatus.FOUND).headers(httpHeaders).build();
    }

    @GetMapping("/cancel/{id}")
    public String cancelPayment(@PathVariable("id") Long id)  {
        String redirectUrl = paymentService.cancelPayment(id);
        return redirectUrl;
    }

    @RequestMapping(value = "/subscription", method = RequestMethod.POST)
    public ResponseEntity<String> createSubscription(@RequestBody CreatePaymentOrSubRequest request) {
        return new ResponseEntity<String>(paymentService.activateSubscription(request), HttpStatus.OK);
    }

    @RequestMapping(value = "/subscription/confirm")
    public ResponseEntity confirmSubscription(@RequestParam("token")String token) throws URISyntaxException {
        paymentService.executeSubAgreement(token);
        URI redirectUrl = new URI("https://localhost:4500/potvrdjeno");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(redirectUrl);
        return ResponseEntity.status(HttpStatus.FOUND).headers(httpHeaders).build();
    }

}
