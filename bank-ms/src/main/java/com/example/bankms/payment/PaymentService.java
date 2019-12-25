package com.example.bankms.payment;

import com.example.bankms.model.Payment;

import java.util.List;

public interface PaymentService {

    List<Payment> findAll();
    PaymentResponse handleKpRequest(PaymentRequest kpRequestDto);
}