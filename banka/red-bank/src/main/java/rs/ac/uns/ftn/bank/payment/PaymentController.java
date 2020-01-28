package rs.ac.uns.ftn.bank.payment;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rs.ac.uns.ftn.bank.account.AccountService;
import rs.ac.uns.ftn.bank.dto.ExecuteTransactionResponse;
import rs.ac.uns.ftn.bank.dto.ExternalBankPaymentRequest;
import rs.ac.uns.ftn.bank.dto.ExternalBankPaymentResponse;
import rs.ac.uns.ftn.bank.model.Payment;
import rs.ac.uns.ftn.bank.card.CardDataDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.url.PccDTO;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/payment")
@CrossOrigin("*")
public class PaymentController {
    private final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    PaymentService paymentService;

    @Autowired
    AccountService accountService;


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
    public ResponseEntity postCardData(@RequestBody CardDataDto cardDataDto, @PathVariable String url) {
        ExecuteTransactionResponse response = paymentService.submitCardData(cardDataDto, url);
        if(response.getSuccessful()){
            return ResponseEntity.ok(response);
        }else{
            return  ResponseEntity.status(400).body(response);
        }
    }

    @RequestMapping(value = "/response", method = RequestMethod.POST)
    public ResponseEntity pccAnswer(@RequestBody PccDTO pccDTO) throws ParseException {
        return new ResponseEntity<>(paymentService.pccAnswer(pccDTO), HttpStatus.OK);
    }
}