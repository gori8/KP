package rs.ac.uns.ftn.bitcoin.cmrs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.bitcoin.dto.*;
import rs.ac.uns.ftn.bitcoin.utils.BitCoinPaymentUtils;
import rs.ac.uns.ftn.url.AmountAndUrlDTO;

import java.util.UUID;

@RestController
@RequestMapping("/api/bitcoin")
@CrossOrigin(origins = "*")
public class BitCoinPaymentController {

    @Autowired
    BitCoinPaymentService bitCoinPaymentService;

    @Autowired
    SellerRepository sellerRepository;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<Long> register(@RequestBody RegistrationDTO registrationDTO) {

        Seller seller = new Seller();
        seller.setUuid(UUID.fromString(registrationDTO.getUuid()));
        seller.setToken(registrationDTO.getToken());
        Long ret = sellerRepository.save(seller).getId();

        return new ResponseEntity<Long>(ret, HttpStatus.OK);
    }

    @RequestMapping(value = "/prepare",method = RequestMethod.POST)
    public ResponseEntity<String> postPreparePayment(@RequestBody PreparePaymentRequest request) {

        PaymentUrlDTO paymentUrlDto = new PaymentUrlDTO();


        CoinGateRequest preparedPayment = bitCoinPaymentService.preparePayment(request);

        CoinGateResponse response = BitCoinPaymentUtils.postOrder(preparedPayment);

        String paymentUrl = response.getPaymentUrl();

        bitCoinPaymentService.saveFromResponse(response, preparedPayment);

        paymentUrlDto.setPaymentUrl(paymentUrl);

        return new ResponseEntity<>("\""+paymentUrlDto.getPaymentUrl()+"\"",HttpStatus.OK);
    }

    @GetMapping("/payment/cancel/{paymentId}")
    public ResponseEntity<?> getPaymentCanceled(@PathVariable Long paymentId) {
        UUID sellerUuid=bitCoinPaymentService.getSellerUUID(paymentId);

        AmountAndUrlDTO amountAndUrlDTO=bitCoinPaymentService.getAmountAndRedirectUrl(sellerUuid.toString());

        String url = bitCoinPaymentService.notifyNc(amountAndUrlDTO.getRedirectUrl()+"/false");

        BitCoinPayment payment = bitCoinPaymentService.getById(paymentId);

        CoinGateResponse response = BitCoinPaymentUtils.getOrder(payment);

        String status = response.getStatus();

        payment.setStatus(status);

        BitCoinPayment savedPayment = bitCoinPaymentService.save(payment);

        String redirectUrl = payment.getRedirectUrl();

        return ResponseEntity.status(HttpStatus.FOUND).header("Location", redirectUrl).build();
    }

    @GetMapping("/payment/success/{paymentId}")
    public ResponseEntity<?> getPaymentSuccess(@PathVariable Long paymentId) {

        UUID sellerUuid=bitCoinPaymentService.getSellerUUID(paymentId);

        AmountAndUrlDTO amountAndUrlDTO=bitCoinPaymentService.getAmountAndRedirectUrl(sellerUuid.toString());

        String url = bitCoinPaymentService.notifyNc(amountAndUrlDTO.getRedirectUrl()+"/true");

        BitCoinPayment payment = bitCoinPaymentService.getById(paymentId);

        CoinGateResponse response = BitCoinPaymentUtils.getOrder(payment);

        String status = response.getStatus();

        payment.setStatus(status);

        BitCoinPayment savedPayment = bitCoinPaymentService.save(payment);

        String redirectUrl = payment.getRedirectUrl();

        return ResponseEntity.status(HttpStatus.FOUND).header("Location", redirectUrl).build();
    }

}
