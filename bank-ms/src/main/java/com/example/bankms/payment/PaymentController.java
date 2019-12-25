package com.example.bankms.payment;


import com.example.bankms.account.AccountService;
import com.example.bankms.account.AccountServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
//@CrossOrigin("http://localhost:3000")
public class PaymentController {

    private final PaymentService paymentService;

    private final AccountService accountService;

    public PaymentController(PaymentServiceImpl paymentService, AccountServiceImpl accountService) {
        this.paymentService = paymentService;
        this.accountService = accountService;
    }

    @PostMapping
    public PaymentResponse paymentRequest(@RequestBody PaymentRequest kpRequestDto) {

        return paymentService.handleKpRequest(kpRequestDto);
    }
}