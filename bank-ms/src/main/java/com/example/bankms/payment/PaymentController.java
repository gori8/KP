package com.example.bankms.payment;


import com.example.bankms.account.AccountService;
import com.example.bankms.account.AccountServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    @RequestMapping(value = "/{url}", method = RequestMethod.POST)
    public Map<String, String> useCardData(@RequestBody CardDataDto cardDataDto, @PathVariable String url) {
        return Map.of("url", paymentService.useCardData(cardDataDto, url));
    }
}