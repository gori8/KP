package rs.ac.uns.ftn.bitcoin.utils;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import rs.ac.uns.ftn.bitcoin.cmrs.BitCoinPayment;
import rs.ac.uns.ftn.bitcoin.cmrs.BitCoinPaymentRepository;
import rs.ac.uns.ftn.bitcoin.dto.CoinGateResponse;
import rs.ac.uns.ftn.bitcoin.dto.CoinGateRequest;


@NoArgsConstructor
public class BitCoinPaymentUtils {
    private static final String API_ORDERS = "https://api-sandbox.coingate.com/v2/orders";

    private static final String AUTHORIZATION = "Authorization";
    private static final String TOKEN = "Token ";
    private static final String PRICE_AMOUNT = "price_amount";
    private static final String PRICE_CURRENCY = "price_currency";
    private static final String RECEIVE_CURRENCY = "receive_currency";
    private static final String TITLE = "title";
    private static final String SUCCESS_URL = "success_url";
    private static final String CANCEL_URL = "cancel_url";
    private static final String PARAMS = "parameters";


    @Autowired
    BitCoinPaymentRepository bitCoinPaymentRepository;

    private String getRedirectUrl(Long paymentId) {

        BitCoinPayment payment = bitCoinPaymentRepository.getOne(paymentId);

        CoinGateResponse response = getOrder(payment);

        String status = response.getStatus();

        payment.setStatus(status);

        BitCoinPayment persistedPayment = bitCoinPaymentRepository.save(payment);

        String redirectUrl = payment.getRedirectUrl();

        return redirectUrl;
    }


    public static CoinGateResponse postOrder(CoinGateRequest request) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION, TOKEN + request.getToken());

        MultiValueMap<String, String> bodyParams = new LinkedMultiValueMap<>();

        bodyParams.add(PRICE_AMOUNT, request.getAmount().toString());
        bodyParams.add(PRICE_CURRENCY, request.getCurrency());
        bodyParams.add(RECEIVE_CURRENCY, request.getCurrency());
        bodyParams.add(TITLE, request.getTitle());
        bodyParams.add(SUCCESS_URL, request.getSuccessUrl());
        System.out.println(request.getSuccessUrl());
        bodyParams.add(CANCEL_URL, request.getCancelUrl());
        System.out.println(request.getCancelUrl());

        HttpEntity<MultiValueMap<String, String>> myRequest = new HttpEntity<>(bodyParams, headers);

        ResponseEntity<CoinGateResponse> re=restTemplate.postForEntity(API_ORDERS, myRequest, CoinGateResponse.class);
        CoinGateResponse ret=re.getBody();
        return ret;
    }

    public static CoinGateResponse getOrder(BitCoinPayment payment) {

        RestTemplate restTemplate=new RestTemplate();

        HttpHeaders headers = new HttpHeaders();

        headers.add(AUTHORIZATION, TOKEN + payment.getSeller().getToken());

        HttpEntity<String> request = new HttpEntity<>(PARAMS, headers);

        ResponseEntity<CoinGateResponse> re=restTemplate.exchange(API_ORDERS + "/" + payment.getOrderId(), HttpMethod.GET, request, CoinGateResponse.class);

        CoinGateResponse ret=re.getBody();
        return ret;
    }
}
