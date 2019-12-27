package rs.ac.uns.ftn.bitcoin.cmrs;

import rs.ac.uns.ftn.bitcoin.dto.CoinGateRequest;
import rs.ac.uns.ftn.bitcoin.dto.CoinGateResponse;
import rs.ac.uns.ftn.bitcoin.dto.PreparePaymentRequest;

public interface BitCoinPaymentService {

    public abstract CoinGateRequest preparePayment(PreparePaymentRequest request);

    public abstract BitCoinPayment saveFromResponse(CoinGateResponse response, CoinGateRequest coinGateRequest);

    public abstract BitCoinPayment getById(Long paymentId);

    public abstract BitCoinPayment save(BitCoinPayment payment);
}
