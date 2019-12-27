package rs.ac.uns.ftn.paypal.cmrs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.paypal.dto.CreatePaymentRequest;
import rs.ac.uns.ftn.paypal.dto.CreatePaymentResponse;
import rs.ac.uns.ftn.paypal.dto.ExecutePaymentRequest;

@RequestMapping("/api/paypal")
@RestController
@CrossOrigin("*")
public class PaymentController {

    @Autowired
    PaymentService paymentService;

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


}
