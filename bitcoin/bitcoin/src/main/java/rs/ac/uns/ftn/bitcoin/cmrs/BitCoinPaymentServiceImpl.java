package rs.ac.uns.ftn.bitcoin.cmrs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.bitcoin.dto.CoinGateRequest;
import rs.ac.uns.ftn.bitcoin.dto.CoinGateResponse;
import rs.ac.uns.ftn.bitcoin.dto.PreparePaymentRequest;

@Service
public class BitCoinPaymentServiceImpl implements BitCoinPaymentService{

    @Autowired
    BitCoinPaymentRepository bitCoinPaymentRepository;

    @Autowired
    SellerRepository sellerRepository;

    private static final String TEST_ORDER = "Testing order";
    private static final String BITCOIN = "BTC";
    private static final String SUCCESS_URL = "http://localhost:8091/api/bitcoin/payment/success/";
    private static final String CANCEL_URL = "http://localhost:8091/api/bitcoin/payment/cancel/";



    @Override
    public CoinGateRequest preparePayment(PreparePaymentRequest request) {

        CoinGateRequest coinGateRequest = new CoinGateRequest();

        Seller seller = sellerRepository.findByEmail(request.getEmail());

        BitCoinPayment savedPayment = bitCoinPaymentRepository.save(new BitCoinPayment());

        coinGateRequest.setPaymentId(savedPayment.getId());

        coinGateRequest.setAmount(request.getAmount());
        coinGateRequest.setToken(seller.getToken());

        coinGateRequest.setCurrency(BITCOIN);

        coinGateRequest.setTitle(TEST_ORDER);

        coinGateRequest.setSuccessUrl(SUCCESS_URL + savedPayment.getId());

        coinGateRequest.setCancelUrl(CANCEL_URL + savedPayment.getId());

        coinGateRequest.setRedirectUrl(request.getRedirectUrl());

        return coinGateRequest;
    }

    @Override
    public BitCoinPayment saveFromResponse(CoinGateResponse response, CoinGateRequest coinGateRequest) {
        BitCoinPayment payment = bitCoinPaymentRepository.getOne(coinGateRequest.getPaymentId());

        Seller seller = sellerRepository.findByToken(coinGateRequest.getToken());

        payment.setOrderId(response.getId());
        payment.setSeller(seller);
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


}
