package rs.ac.uns.ftn.fourthms.payment;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.fourthms.dto.PreparePaymentRequest;

import java.net.URISyntaxException;

@RequestMapping("/api/fourth")
@RestController
@CrossOrigin("*")
public class FourthMsController {

    private final Logger LOGGER = LoggerFactory.getLogger(FourthMsController.class);


    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> postPreparePayment(@RequestBody PreparePaymentRequest request) {

        LOGGER.info("Preparing payment on fourth micro service...");
        LOGGER.info("Amount of payment: " + request.getAmount());

        return new ResponseEntity<>("\""+"https://localhost:4600/payment"+"\"", HttpStatus.OK);
    }

    @GetMapping("/payment/cancel/{paymentId}")
    public ResponseEntity<?> getPaymentCanceled(@PathVariable Long paymentId) {

        LOGGER.info("Canceling payment on fourth micro service...");

        return ResponseEntity.status(HttpStatus.FOUND).build();
    }

    @GetMapping("/payment/success/{paymentId}")
    public ResponseEntity<?> getPaymentSuccess(@PathVariable Long paymentId) {

        LOGGER.info("Successful payment on fourth micro service...");

        return ResponseEntity.status(HttpStatus.FOUND).build();
    }

    @RequestMapping(value = "/execute", method = RequestMethod.GET)
    public ResponseEntity create(@RequestParam("paymentId")String paymentId,@RequestParam("token")String token,@RequestParam("PayerID")String payerId ) throws URISyntaxException {

        return ResponseEntity.status(HttpStatus.FOUND).build();
    }

}
