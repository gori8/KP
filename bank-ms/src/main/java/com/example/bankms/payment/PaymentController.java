package com.example.bankms.payment;


import org.springframework.web.bind.annotation.*;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/payment")
@CrossOrigin("*")
public class PaymentController {

    private final PaymentService paymentService;


    public PaymentController(PaymentServiceImpl paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<String> paymentRequest(@RequestBody PaymentRequest kpRequestDto) {

        return new ResponseEntity<String>("\""+paymentService.handleKpRequest(kpRequestDto)+"\"", HttpStatus.OK);
    }

    @RequestMapping(value = "/{url}", method = RequestMethod.POST)
    public ResponseEntity<String> useCardData(@RequestBody CardDataDto cardDataDto, @PathVariable String url) {
        return new ResponseEntity<String>("\""+paymentService.useCardData(cardDataDto, url)+"\"", HttpStatus.OK);
    }
}