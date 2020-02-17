package rs.ac.uns.ftn.paypal.cmrs;

import com.paypal.api.payments.Plan;
import rs.ac.uns.ftn.paypal.dto.CreatePaymentOrSubRequest;
import rs.ac.uns.ftn.paypal.dto.CreatePaymentOrSubResponse;
import rs.ac.uns.ftn.paypal.dto.ExecutePaymentRequest;
import rs.ac.uns.ftn.paypal.dto.SubPlanDTO;
import rs.ac.uns.ftn.url.AmountAndUrlDTO;
import rs.ac.uns.ftn.url.PayPalSubscriptionDTO;

import java.math.BigDecimal;

public interface PaymentService {

    CreatePaymentOrSubResponse createPayment(CreatePaymentOrSubRequest kpRequest);

    String executePayment(ExecutePaymentRequest request);

    String cancelPayment(Long id);

    void cancelSubscription(Long id);

    Plan createBillingPlan(CreatePaymentOrSubRequest request, AmountAndUrlDTO amountAndUrlDTO, BigDecimal cena, String finishRedirectUrl);

    String activateSubscription(SubPlanDTO subPlanRequest);

    String executeSubAgreement(Long subscriptionId, String token);

    void updateStatusOrRetryCapture();

    void updateIntegratedSoftwareStatus();

    String createSubPlanUrl(PayPalSubscriptionDTO request);
}

