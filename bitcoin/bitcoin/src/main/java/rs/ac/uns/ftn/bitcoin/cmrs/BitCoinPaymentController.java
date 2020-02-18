package rs.ac.uns.ftn.bitcoin.cmrs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.bitcoin.BitcoinApplication;
import rs.ac.uns.ftn.bitcoin.dto.*;
import rs.ac.uns.ftn.bitcoin.utils.BitCoinPaymentUtils;
import rs.ac.uns.ftn.url.AmountAndUrlDTO;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/bitcoin")
@CrossOrigin(origins = "*")
public class BitCoinPaymentController {

    @Autowired
    BitCoinPaymentService bitCoinPaymentService;

    @Autowired
    SellerRepository sellerRepository;

    @Autowired
    BitCoinPaymentUtils bitCoinPaymentUtils;

    private final Logger LOGGER = LoggerFactory.getLogger(BitCoinPaymentController.class);

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> postPreparePayment(@RequestBody PreparePaymentRequest request) {

        PaymentUrlDTO paymentUrlDto = new PaymentUrlDTO();


        CoinGateRequest preparedPayment = bitCoinPaymentService.preparePayment(request);

        CoinGateResponse response = bitCoinPaymentUtils.postOrder(preparedPayment);

        String paymentUrl = response.getPaymentUrl();

        bitCoinPaymentService.saveFromResponse(response, preparedPayment);

        paymentUrlDto.setPaymentUrl(paymentUrl);

        LOGGER.info(LocalDateTime.now() + "      Preparing payment on bitcoin microservice...");

        return new ResponseEntity<>("\""+paymentUrlDto.getPaymentUrl()+"\"",HttpStatus.OK);
    }

    @GetMapping("/payment/cancel/{paymentId}")
    public ResponseEntity<?> getPaymentCanceled(@PathVariable Long paymentId) {
        BitCoinPayment bitCoinPayment=bitCoinPaymentService.getById(paymentId);

        AmountAndUrlDTO amountAndUrlDTO=bitCoinPaymentService.getAmountAndRedirectUrl(bitCoinPayment.getRedUrlUuid().toString());

        String url = bitCoinPaymentService.notifyNc(amountAndUrlDTO.getRedirectUrl()+"/false");

        BitCoinPayment payment = bitCoinPaymentService.getById(paymentId);

        CoinGateResponse response = bitCoinPaymentUtils.getOrder(payment);

        String status = response.getStatus();

        payment.setStatus(status);

        BitCoinPayment savedPayment = bitCoinPaymentService.save(payment);

        LOGGER.info(LocalDateTime.now() + "      Canceling payment with payment id: " + paymentId + " on bitcoin microservice...");

        return ResponseEntity.status(HttpStatus.FOUND).header("Location", url).build();
    }

    @GetMapping("/payment/success/{paymentId}")
    public ResponseEntity<?> getPaymentSuccess(@PathVariable Long paymentId) {

        BitCoinPayment bitCoinPayment=bitCoinPaymentService.getById(paymentId);

        AmountAndUrlDTO amountAndUrlDTO=bitCoinPaymentService.getAmountAndRedirectUrl(bitCoinPayment.getRedUrlUuid().toString());

        String url = bitCoinPaymentService.notifyNc(amountAndUrlDTO.getRedirectUrl()+"/true");

        BitCoinPayment payment = bitCoinPaymentService.getById(paymentId);

        CoinGateResponse response = bitCoinPaymentUtils.getOrder(payment);

        String status = response.getStatus();

        payment.setStatus(status);

        BitCoinPayment savedPayment = bitCoinPaymentService.save(payment);

        LOGGER.info(LocalDateTime.now() + "      Successful payment with payment id: " + paymentId + " on bitcoin microservice...");

        return ResponseEntity.status(HttpStatus.FOUND).header("Location", url).build();
    }

}
