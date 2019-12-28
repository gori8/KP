package rs.ac.uns.ftn.paypal.cmrs;

import rs.ac.uns.ftn.paypal.dto.CreatePaymentRequest;
import rs.ac.uns.ftn.paypal.dto.CreatePaymentResponse;
import rs.ac.uns.ftn.paypal.dto.ExecutePaymentRequest;

public interface PaymentService {

    public abstract CreatePaymentResponse createPayment(CreatePaymentRequest kpRequest);

    public abstract String executePayment(ExecutePaymentRequest request);

    public abstract String cancelPayment(Long id);
}

