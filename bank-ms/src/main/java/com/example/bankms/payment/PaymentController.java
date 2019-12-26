package com.example.bankms.payment;


import com.example.bankms.account.AccountService;
import com.example.bankms.account.AccountServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/payment")
@CrossOrigin("*")
public class PaymentController {

    private final PaymentService paymentService;

    private final AccountService accountService;

    public PaymentController(PaymentServiceImpl paymentService, AccountServiceImpl accountService) {
        this.paymentService = paymentService;
        this.accountService = accountService;
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