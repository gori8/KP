package com.example.bankms.payment;

import com.example.bankms.model.Payment;

import java.util.List;

public interface PaymentService {

    List<Payment> findAll();
    String useCardData(CardDataDto cardDataDto, String url);
    String handleKpRequest(PaymentRequest kpRequestDto);

}