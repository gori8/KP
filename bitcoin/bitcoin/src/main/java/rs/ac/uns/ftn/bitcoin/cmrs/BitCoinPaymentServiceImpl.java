package rs.ac.uns.ftn.bitcoin.cmrs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rs.ac.uns.ftn.bitcoin.dto.CoinGateRequest;
import rs.ac.uns.ftn.bitcoin.dto.CoinGateResponse;
import rs.ac.uns.ftn.bitcoin.dto.PreparePaymentRequest;
import rs.ac.uns.ftn.url.AmountAndUrlDTO;
import rs.ac.uns.ftn.url.UrlClass;

import java.util.UUID;

@Service
public class BitCoinPaymentServiceImpl implements BitCoinPaymentService{

    @Autowired
    BitCoinPaymentRepository bitCoinPaymentRepository;

    @Autowired
    SellerRepository sellerRepository;

    private static final String TEST_ORDER = "Testing order";
    private static final String BITCOIN = "BTC";




    @Override
    public CoinGateRequest preparePayment(PreparePaymentRequest request) {

        AmountAndUrlDTO amountAndUrlDTO=getAmountAndRedirectUrl(request.getCasopisUuid());


        CoinGateRequest coinGateRequest = new CoinGateRequest();

        Seller seller = sellerRepository.findBySellerEmail(amountAndUrlDTO.getSellerEmail());

        BitCoinPayment savedPayment = new BitCoinPayment();
        savedPayment.setItemId(UUID.fromString(amountAndUrlDTO.getItemUuid()));
        savedPayment.setSellerEmail(amountAndUrlDTO.getSellerEmail());
        savedPayment.setRedUrlUuid(UUID.fromString(request.getCasopisUuid()));
        savedPayment = bitCoinPaymentRepository.save(savedPayment);

        coinGateRequest.setPaymentId(savedPayment.getId());

        coinGateRequest.setAmount(amountAndUrlDTO.getAmount());
        coinGateRequest.setToken(seller.getToken());

        coinGateRequest.setCurrency(BITCOIN);

        coinGateRequest.setTitle(TEST_ORDER);

        coinGateRequest.setSuccessUrl(UrlClass.SUCCESS_URL_BTC + savedPayment.getId());

        coinGateRequest.setCancelUrl(UrlClass.CANCEL_URL_BTC + savedPayment.getId());

        coinGateRequest.setEmail(amountAndUrlDTO.getSellerEmail());

        String rurl=amountAndUrlDTO.getRedirectUrl();


        coinGateRequest.setRedirectUrl(rurl);



        return coinGateRequest;
    }

    @Override
    public BitCoinPayment saveFromResponse(CoinGateResponse response, CoinGateRequest coinGateRequest) {
        BitCoinPayment payment = bitCoinPaymentRepository.getOne(coinGateRequest.getPaymentId());

        Seller seller = sellerRepository.findBySellerEmail(coinGateRequest.getEmail());

        payment.setOrderId(response.getId());
        payment.setSellerEmail(seller.getSellerEmail());
        payment.setAmount(coinGateRequest.getAmount());
        payment.setRedirectUrl(coinGateRequest.getRedirectUrl());
        payment.setStatus(response.getStatus());

        return bitCoinPaymentRepository.save(payment);
    }

    @Override
    public BitCoinPayment getById(Long paymentId) {
        return bitCoinPaymentRepository.getOne(paymentId);
    }

    @Override
    public BitCoinPayment save(BitCoinPayment payment) {
        return bitCoinPaymentRepository.save(payment);
    }

    @Autowired
    RestTemplate restTemplate;

    @Override
    public  AmountAndUrlDTO getAmountAndRedirectUrl(String casopisID){
        String url=UrlClass.DOBAVI_CENU_URL_SA_PAYMENT_INFO+casopisID;
        System.out.println(url);
        ResponseEntity<AmountAndUrlDTO> resp
                = restTemplate.getForEntity(url, AmountAndUrlDTO.class);
        return resp.getBody();
    }

    @Override
    public String notifyNc(String url){
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        HttpEntity entity=new HttpEntity("",headers);
        ResponseEntity<String> redirectUrl=restTemplate.postForEntity(url,entity,String.class);
        System.out.println(redirectUrl);
        return redirectUrl.getBody();
    }

    @Override
    public String getSellerEmail(Long id){
        BitCoinPayment payment=bitCoinPaymentRepository.getOne(id);
        return payment.getSellerEmail();
    }



}
