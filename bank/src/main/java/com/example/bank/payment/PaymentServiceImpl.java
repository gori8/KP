package com.example.bank.payment;

import com.example.bank.account.AccountService;
import com.example.bank.card.CardService;
import com.example.bank.client.ClientService;
import com.example.bank.model.*;
import com.example.bank.properties.BankProperties;
import com.example.bank.transaction.TransactionService;
import com.example.bank.card.CardDataDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class PaymentServiceImpl implements PaymentService {
    private static final String PAYMENT_URL_F = "%s/card/%s";

    private final Logger LOGGER = LoggerFactory.getLogger(PaymentServiceImpl.class);
    public static final String NOT_FOUND = "notFound";


    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ClientService clientService;

    @Autowired
    private CardService cardService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private BankProperties properties;

    @Override
    public ExternalBankPaymentResponse handleKpRequest(ExternalBankPaymentRequest kpRequestDto) {

        Payment payment = new Payment();
        Payment savedPayment = new Payment();

        ExternalBankPaymentResponse response = new ExternalBankPaymentResponse();

        Client client = clientService.findByMerchantId(kpRequestDto.getMerchantId());

        if (client == null) {
            response.setUrl(NOT_FOUND);
        } else {
            payment.setUrl(generateTimeStamp());
            payment.setAmount(kpRequestDto.getAmount());
            payment.setMerchant(client.getAccount());
            payment.setSuccessUrl(kpRequestDto.getSuccessUrl());
            payment.setFailedUrl(kpRequestDto.getFailedUrl());
            payment.setErrorUrl(kpRequestDto.getErrorUrl());

            savedPayment = paymentRepository.save(payment);
        }

        response.setUrl(generateRedirectUrl(savedPayment.getUrl()));
        response.setId(savedPayment.getId());

        return response;
    }

    @Override
    public String submitCardData(CardDataDto cardDataDto, String url) {

        System.out.println(cardDataDto.getPan() + cardDataDto.getHolderName() + cardDataDto.getSecurityCode() + cardDataDto.getValidTo());
        System.out.println(url);

        Transaction transaction = new Transaction();
        String redirectUrl;

        Payment payment = paymentRepository.findByUrl(url);
        System.out.println(payment.getAmount().toString() + payment.getMerchant() + payment.getUrl() + payment.getId() + payment.getSuccessUrl());

        if (payment == null) {
            transaction.setValid(false);
            transaction.setAmount(BigDecimal.valueOf(0));
            transactionService.save(transaction);

            return payment.getErrorUrl();
        }

        Card card = cardService.findByPan(cardDataDto.getPan());
        System.out.println("PAN:    " + cardDataDto.getPan());
        System.out.println("CARD:   " + card);

        if (card == null) {
            transaction.setValid(false);
            transaction.setAmount(BigDecimal.valueOf(0));
            transaction.setRecipient(payment.getMerchant());
            transactionService.save(transaction);
            return payment.getErrorUrl();
        }

        if (!card.getSecurityCode().toString().equals(cardDataDto.getSecurityCode().toString())) {
            transaction.setValid(false);
            transaction.setAmount(BigDecimal.valueOf(0));
            transaction.setRecipient(payment.getMerchant());
            transactionService.save(transaction);
            return payment.getErrorUrl();
        }

        if(!card.getValidTo().toString().equals(cardDataDto.getValidTo().toString())) {
            transaction.setValid(false);
            transaction.setAmount(BigDecimal.valueOf(0));
            transaction.setRecipient(payment.getMerchant());
            transactionService.save(transaction);
            return payment.getErrorUrl();
        }

        transaction.setRecipient(payment.getMerchant());
        transaction.setPayer(card.getAccount());
        transaction.setAmount(payment.getAmount());

        Account merchant = payment.getMerchant();
        Account account = card.getAccount();

        if (account.getAmount().compareTo(payment.getAmount()) < 0) {
            transaction.setValid(false);
            redirectUrl = payment.getFailedUrl();

        } else {
            redirectUrl = payment.getSuccessUrl();

            account.setAmount(account.getAmount().subtract(payment.getAmount()));
            merchant.setAmount(merchant.getAmount().add(payment.getAmount()));

            accountService.save(account);
            accountService.save(merchant);

            transaction.setValid(true);
        }
        transactionService.save(transaction);


        return redirectUrl;
    }

    @Override
    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    @Override
    public Payment findByUrl(String url) {
        return paymentRepository.findByUrl(url);
    }

    private String generateTimeStamp() {
        return String.valueOf(System.currentTimeMillis() + ThreadLocalRandom.current().nextInt(10, 99));
    }

    private String generateRedirectUrl(String code) {
        return String.format(PAYMENT_URL_F, properties.getUrl(), code);
    }
}