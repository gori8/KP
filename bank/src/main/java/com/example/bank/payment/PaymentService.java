package com.example.bank.payment;


import com.example.bank.model.Payment;
import com.example.bank.card.CardDataDto;

import java.util.List;

public interface PaymentService {
    List<Payment> findAll();

    Payment findByUrl(String url);

    ExternalBankPaymentResponse handleKpRequest(ExternalBankPaymentRequest kpRequestDto);

    String submitCardData(CardDataDto cardDataDto, String url);

}
