package rs.ac.uns.ftn.bank.payment;


import rs.ac.uns.ftn.bank.dto.ExecuteTransactionResponse;
import rs.ac.uns.ftn.bank.dto.ExternalBankPaymentRequest;
import rs.ac.uns.ftn.bank.dto.ExternalBankPaymentResponse;
import rs.ac.uns.ftn.bank.model.Payment;
import rs.ac.uns.ftn.bank.card.CardDataDto;

import java.util.List;

public interface PaymentService {
    List<Payment> findAll();

    Payment findByUrl(String url);

    ExternalBankPaymentResponse handleKpRequest(ExternalBankPaymentRequest kpRequestDto);

    ExecuteTransactionResponse submitCardData(CardDataDto cardDataDto, String url);

}
