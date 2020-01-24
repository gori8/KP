package rs.ac.uns.ftn.kp.bankms.payment;

import rs.ac.uns.ftn.kp.bankms.model.Payment;

import java.util.List;

public interface PaymentService {

    List<Payment> findAll();
    String handleKpRequest(PaymentRequest kpRequestDto);
    String setPaymentStatus(String id,String status);

}