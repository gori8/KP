package rs.ac.uns.ftn.bank.payment;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import rs.ac.uns.ftn.bank.account.AccountRepository;
import rs.ac.uns.ftn.bank.account.AccountService;
import rs.ac.uns.ftn.bank.card.CardService;
import rs.ac.uns.ftn.bank.client.ClientRepository;
import rs.ac.uns.ftn.bank.client.ClientService;
import rs.ac.uns.ftn.bank.dto.ExecuteTransactionResponse;
import rs.ac.uns.ftn.bank.dto.ExternalBankPaymentRequest;
import rs.ac.uns.ftn.bank.dto.ExternalBankPaymentResponse;
import rs.ac.uns.ftn.bank.model.*;
import rs.ac.uns.ftn.bank.properties.BankProperties;
import rs.ac.uns.ftn.bank.transaction.TransactionService;
import rs.ac.uns.ftn.bank.card.CardDataDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.url.PccDTO;
import rs.ac.uns.ftn.url.PccEntity;
import rs.ac.uns.ftn.url.TransactionStatus;
import rs.ac.uns.ftn.url.UrlClass;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {
    private static final String PAYMENT_URL_F = UrlClass.FRONT_BANKE+"%s/card/%s";

    private final Logger LOGGER = LoggerFactory.getLogger(PaymentServiceImpl.class);
    public static final String NOT_FOUND = "notFound";

    private static final String BANK_CODE = "381311";


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

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ClientRepository clientRepository;

    @Override
    public ExternalBankPaymentResponse handleKpRequest(ExternalBankPaymentRequest kpRequestDto) {

        Payment payment = new Payment();
        Payment savedPayment = new Payment();

        LOGGER.info("Processing request from Bank Microservice");

        ExternalBankPaymentResponse response = new ExternalBankPaymentResponse();

        LOGGER.info("Finding client that will receive payment");
        Client client = clientService.findByMerchantId(kpRequestDto.getMerchantId());

        if (client == null) {
            LOGGER.error("Client with provided merchant id does not exists");
            response.setUrl(NOT_FOUND);
        } else {
            LOGGER.info("Client with provided merchant id was found");

            LOGGER.info("Creating payment");
            payment.setUrl(generateRandomUrl());
            payment.setAmount(kpRequestDto.getAmount());
            payment.setMerchant(client.getAccount());
            payment.setSuccessUrl(kpRequestDto.getSuccessUrl());
            payment.setFailedUrl(kpRequestDto.getFailedUrl());
            payment.setErrorUrl(kpRequestDto.getErrorUrl());
            payment.setTimeStamp(new Date());

            savedPayment = paymentRepository.save(payment);
            LOGGER.info("Payment saved in bank database");
        }

        LOGGER.info("Generating response for Bank Microservice with payment url");

        response.setUrl(generateRedirectUrl(savedPayment.getUrl()));
        response.setId(savedPayment.getId());

        LOGGER.info("Generated response: " + response.toString());

        return response;
    }

    @Override
    public ExecuteTransactionResponse submitCardData(CardDataDto cardDataDto, String url) {

        System.out.println(cardDataDto.getPan() + cardDataDto.getHolderName() + cardDataDto.getSecurityCode() + cardDataDto.getValidTo());
        System.out.println(url);

        ExecuteTransactionResponse response = new ExecuteTransactionResponse();

        LOGGER.info("Creating transaction");

        Transaction transaction = new Transaction();
        String redirectUrl;

        LOGGER.info("Trying to find payment on provided url..");
        Payment payment = paymentRepository.findByUrl(url);
        System.out.println(payment.getAmount().toString() + payment.getMerchant() + payment.getUrl() + payment.getId() + payment.getSuccessUrl());

        if (payment == null) {
            LOGGER.error("Could not find payment on provided url: " + url);
            LOGGER.info("Returning error-url to Bank Microservice");
            transaction.setValid(false);
            transaction.setAmount(BigDecimal.valueOf(0));
            transactionService.save(transaction);

            ResponseEntity<String> redirectUrlResponse
                    = restTemplate.getForEntity(payment.getErrorUrl(),String.class);
            response.setUrl(redirectUrlResponse.getBody());
            response.setSuccessful(false);
            return response;
        }

        LOGGER.info("Trying to find card with provided pan number..");

        Card card = cardService.findByPan(cardDataDto.getPan());
        System.out.println("PAN:    " + cardDataDto.getPan());
        System.out.println("CARD:   " + card);

        if (card == null) {
            if(callPcc(cardDataDto,payment.getId(),new Date(),payment.getAmount()).equals(TransactionStatus.SUCCESSFUL)){
                ResponseEntity<String> redirectUrlResponse
                        = restTemplate.getForEntity(payment.getSuccessUrl(),String.class);
                redirectUrl = redirectUrlResponse.getBody();

                Account merchant = payment.getMerchant();

                Client pccClient=clientRepository.findByMerchantId("pcc");
                Account pccAccount=pccClient.getAccount();

                pccAccount.setAmount(pccAccount.getAmount().subtract(payment.getAmount()));
                merchant.setAmount(merchant.getAmount().add(payment.getAmount()));

                transaction.setRecipient(merchant);
                transaction.setPayer(pccAccount);
                transaction.setAmount(payment.getAmount());

                accountService.save(pccAccount);
                accountService.save(merchant);

                transaction.setValid(true);
                transactionService.save(transaction);

                response.setUrl(redirectUrl);
                response.setSuccessful(transaction.getValid());

                return response;
            }
            LOGGER.error("Could not find card with provided pan number: " + cardDataDto.getPan());
            LOGGER.info("Returning error-url");

            transaction.setValid(false);
            transaction.setAmount(BigDecimal.valueOf(0));
            transaction.setRecipient(payment.getMerchant());
            transactionService.save(transaction);

            ResponseEntity<String> redirectUrlResponse
                    = restTemplate.getForEntity(payment.getErrorUrl(),String.class);
            response.setUrl(redirectUrlResponse.getBody());
            response.setSuccessful(false);
            return response;
        }

        if (!card.getSecurityCode().toString().equals(cardDataDto.getSecurityCode().toString())) {
            LOGGER.error("Provided Security Code does not match for provided Card.");
            LOGGER.info("Returning error-url to Bank Microservice");
            transaction.setValid(false);
            transaction.setAmount(BigDecimal.valueOf(0));
            transaction.setRecipient(payment.getMerchant());
            transactionService.save(transaction);

            ResponseEntity<String> redirectUrlResponse
                    = restTemplate.getForEntity(payment.getErrorUrl(),String.class);
            response.setUrl(redirectUrlResponse.getBody());
            response.setSuccessful(false);
            return response;
        }

        if(!card.getValidTo().toString().equals(cardDataDto.getValidTo().toString())) {
            LOGGER.error("Provided Valid to Date do not match for provided Card.");
            LOGGER.info("Returning error-url to Bank Microservice");
            transaction.setValid(false);
            transaction.setAmount(BigDecimal.valueOf(0));
            transaction.setRecipient(payment.getMerchant());
            transactionService.save(transaction);

            ResponseEntity<String> redirectUrlResponse
                    = restTemplate.getForEntity(payment.getErrorUrl(),String.class);
            response.setUrl(redirectUrlResponse.getBody());
            response.setSuccessful(false);
            return response;
        }

        LOGGER.info("Provided Card Data is valid.");

        transaction.setRecipient(payment.getMerchant());
        transaction.setPayer(card.getAccount());
        transaction.setAmount(payment.getAmount());

        Account merchant = payment.getMerchant();
        Account account = card.getAccount();

        LOGGER.info("Checking if there is enough funding for payment..");
        if (account.getAmount().compareTo(payment.getAmount()) < 0) {
            LOGGER.error("There is not enough funds on Card.");
            transaction.setValid(false);
            ResponseEntity<String> redirectUrlResponse
                    = restTemplate.getForEntity(payment.getFailedUrl(),String.class);
            redirectUrl = redirectUrlResponse.getBody();
        } else {
            ResponseEntity<String> redirectUrlResponse
                    = restTemplate.getForEntity(payment.getSuccessUrl(),String.class);
            redirectUrl = redirectUrlResponse.getBody();

            account.setAmount(account.getAmount().subtract(payment.getAmount()));
            merchant.setAmount(merchant.getAmount().add(payment.getAmount()));

            accountService.save(account);
            accountService.save(merchant);

            transaction.setValid(true);
        }
        transactionService.save(transaction);
        LOGGER.info("Payment transaction is saved in bank database.");

        response.setUrl(redirectUrl);
        response.setSuccessful(transaction.getValid());
        return response;
    }

    @Override
    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    @Override
    public Payment findByUrl(String url) {
        return paymentRepository.findByUrl(url);
    }

    private String generateRandomUrl() {
        return UUID.randomUUID().toString();
    }

    private String generateRedirectUrl(String code) {
        return String.format(PAYMENT_URL_F, "banka", code);
    }

    private TransactionStatus callPcc(CardDataDto cardDataDto, Long orderId, Date timeStamp,BigDecimal amount){
        PccDTO pccDTO = new PccDTO();
        pccDTO.setPan(cardDataDto.getPan());
        pccDTO.setHolderName(cardDataDto.getHolderName());
        pccDTO.setSecurityCode(cardDataDto.getSecurityCode());
        pccDTO.setValidTo(cardDataDto.getValidTo().toString());
        pccDTO.setAcquirerOrderId(orderId.toString());
        pccDTO.setAcquirerTimestamp(timeStamp);
        pccDTO.setBankCode(BANK_CODE);
        pccDTO.setAmount(amount);

        LOGGER.info(pccDTO.getValidTo().toString());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<PccDTO> entity = new HttpEntity<PccDTO>(pccDTO, headers);

        ResponseEntity<TransactionStatus> response =
                restTemplate.postForEntity(UrlClass.PCC_URL+"transaction",entity,TransactionStatus.class);

        TransactionStatus status = response.getBody();
        return status;
    }

    @Override
    public PccEntity pccAnswer(PccDTO pccDTO) throws ParseException {

        Transaction transaction = new Transaction();

        Card card = cardService.findByPan(pccDTO.getPan());
        System.out.println("PAN:    " + pccDTO.getPan());
        System.out.println("CARD:   " + card);
        LOGGER.info("Valid to sent:   "+pccDTO.getValidTo());
        LOGGER.info("Valid to card:   "+card.getValidTo());

        Client client = clientRepository.findByMerchantId("pcc");
        Account recipientAccount=client.getAccount();

        PccEntity pccEntity=new PccEntity();
        Date timestamp=new Date();
        pccEntity.setIssuerTimeStamp(timestamp);

        if (card == null) {
            LOGGER.error("Could not find card with provided pan number: " + pccDTO.getPan());
            LOGGER.info("Returning error-url to Bank Microservice");

            transaction.setValid(false);
            transaction.setAmount(BigDecimal.valueOf(0));
            transaction.setRecipient(recipientAccount);
            transactionService.save(transaction);

            pccEntity.setIssuerOrderId(transaction.getId());
            pccEntity.setStatus(TransactionStatus.ERROR);
            return pccEntity;
        }

        if (!card.getSecurityCode().toString().equals(pccDTO.getSecurityCode().toString())) {
            LOGGER.error("Provided Security Code does not match for provided Card.");
            LOGGER.info("Returning error-url to Bank Microservice");
            transaction.setValid(false);
            transaction.setAmount(BigDecimal.valueOf(0));
            transaction.setRecipient(recipientAccount);
            transactionService.save(transaction);

            pccEntity.setIssuerOrderId(transaction.getId());
            pccEntity.setStatus(TransactionStatus.ERROR);
            return pccEntity;
        }

        if(!card.getValidTo().toString().equals(pccDTO.getValidTo().toString())) {
            LOGGER.error("Provided Valid to Date do not match for provided Card.");
            LOGGER.info("Returning error-url to Bank Microservice");
            transaction.setValid(false);
            transaction.setAmount(BigDecimal.valueOf(0));
            transaction.setRecipient(recipientAccount);
            transactionService.save(transaction);

            pccEntity.setIssuerOrderId(transaction.getId());
            pccEntity.setStatus(TransactionStatus.ERROR);
            return pccEntity;
        }

        LOGGER.info("Provided Card Data is valid.");


        transaction.setRecipient(recipientAccount);
        transaction.setPayer(card.getAccount());
        transaction.setAmount(pccDTO.getAmount());

        Account account = card.getAccount();

        TransactionStatus ret = null;
        LOGGER.info("Checking if there is enough funding for payment..");
        if (account.getAmount().compareTo(pccDTO.getAmount()) < 0) {
            LOGGER.error("There is not enough funds on Card.");
            transaction.setValid(false);
            pccEntity.setStatus(TransactionStatus.FAILED);
        } else {
            account.setAmount(account.getAmount().subtract(pccDTO.getAmount()));
            recipientAccount.setAmount(recipientAccount.getAmount().add(pccDTO.getAmount()));
            accountService.save(account);
            accountService.save(recipientAccount);

            transaction.setValid(true);

            pccEntity.setStatus(TransactionStatus.SUCCESSFUL);
        }
        transaction = transactionService.save(transaction);
        pccEntity.setIssuerOrderId(transaction.getId());
        LOGGER.info("Payment transaction is saved in bank database.");
        return pccEntity;
    }
}