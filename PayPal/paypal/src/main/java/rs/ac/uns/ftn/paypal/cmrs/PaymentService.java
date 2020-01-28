package rs.ac.uns.ftn.paypal.cmrs;

import com.paypal.api.payments.Plan;
import rs.ac.uns.ftn.paypal.dto.CreatePaymentOrSubRequest;
import rs.ac.uns.ftn.paypal.dto.CreatePaymentOrSubResponse;
import rs.ac.uns.ftn.paypal.dto.ExecutePaymentRequest;

public interface PaymentService {

    CreatePaymentOrSubResponse createPayment(CreatePaymentOrSubRequest kpRequest);

    String executePayment(ExecutePaymentRequest request);

    String cancelPayment(Long id);

    Plan createBillingPlan(CreatePaymentOrSubRequest request);

    String activateSubscription(CreatePaymentOrSubRequest request);

    void executeSubAgreement(String token);
}

