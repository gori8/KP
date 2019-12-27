package com.example.bank.payment;


import com.example.bank.account.AccountService;
import com.example.bank.account.AccountServiceImpl;
import com.example.bank.model.Payment;
import com.example.bank.card.CardDataDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/payment")
@CrossOrigin("*")
public class PaymentController {
    private final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    private final PaymentService paymentService;

    private final AccountService accountService;

    public PaymentController(PaymentServiceImpl paymentService, AccountServiceImpl accountService) {
        this.paymentService = paymentService;
        this.accountService = accountService;
    }

    @PostMapping
    public ExternalBankPaymentResponse postPaymentRequest(@RequestBody ExternalBankPaymentRequest kpRequestDto) {
        logger.debug("Requesting payment");
        logger.debug(String.format("Request: \n %s", kpRequestDto.toString()));

        return paymentService.handleKpRequest(kpRequestDto);
    }

    @GetMapping("/{url}")
    public Payment getPaymentPage(@PathVariable String url) {
        return paymentService.findByUrl(url);
    }

    @RequestMapping(value = "/{url}", method = RequestMethod.POST)
    public Map<String, String> postCardData(@RequestBody CardDataDto cardDataDto, @PathVariable String url) {
        List<String> lista = paymentService.submitCardData(cardDataDto, url);
        return Map.of("url", lista.iterator().next(), "placeno", lista.get(1));
    }
}