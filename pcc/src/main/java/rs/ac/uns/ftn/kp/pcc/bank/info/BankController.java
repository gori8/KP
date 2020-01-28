package rs.ac.uns.ftn.kp.pcc.bank.info;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import rs.ac.uns.ftn.kp.pcc.bank.request.TransactionRequest;
import rs.ac.uns.ftn.kp.pcc.bank.request.TransactionRequestRepository;
import rs.ac.uns.ftn.kp.pcc.dto.RegisterBankDTO;
import rs.ac.uns.ftn.url.PccDTO;
import rs.ac.uns.ftn.url.TransactionStatus;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@RestController
@RequestMapping("/api/pcc")
//@CrossOrigin(origins = "*")
public class BankController {

    @Autowired
    BankRepository bankRepository;

    @Autowired
    TransactionRequestRepository transactionRequestRepository;

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<Bank> registerBank(@RequestBody RegisterBankDTO registrationDTO) {

        Bank bank = new Bank();
        bank.setBankCode(registrationDTO.getBankCode());
        bank.setBankUrl(registrationDTO.getBankUrl());
        Bank ret = bankRepository.save(bank);

        return new ResponseEntity<Bank>(ret, HttpStatus.OK);
    }

    @RequestMapping(value = "/transaction", method = RequestMethod.POST)
    public ResponseEntity<TransactionStatus> makeResponse(@RequestBody PccDTO requestFromAcquirer) throws ParseException {


        String pan = requestFromAcquirer.getPan();
        pan = pan.substring(1, 7);

        System.out.println(requestFromAcquirer.getValidTo());


        TransactionRequest transactionRequest = saveRequest(requestFromAcquirer);

        Bank bank = bankRepository.findOneByBankCode(pan);

        if(bank==null){
            transactionRequest.setStatus(TransactionStatus.ERROR);
            transactionRequestRepository.save(transactionRequest);
            return ResponseEntity.ok(TransactionStatus.ERROR);
        }
        ResponseEntity<TransactionStatus> response = restTemplate.postForEntity(bank.getBankUrl(),requestFromAcquirer,TransactionStatus.class);
        transactionRequest.setStatus(response.getBody());
        transactionRequestRepository.save(transactionRequest);
        return response;
    }

    public TransactionRequest saveRequest(PccDTO requestFromAcquirer) throws ParseException {

        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");

        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setAcquirerOrderId(requestFromAcquirer.getAcquirerOrderId());
        transactionRequest.setAcquirerTimestamp(requestFromAcquirer.getAcquirerTimestamp());
        transactionRequest.setHolderName(requestFromAcquirer.getHolderName());
        transactionRequest.setPan(requestFromAcquirer.getPan());
        transactionRequest.setSecurityCode(requestFromAcquirer.getSecurityCode());
        transactionRequest.setValidTo(formatter.parse(requestFromAcquirer.getValidTo()));
        transactionRequest.setStatus(TransactionStatus.CREATED);

        return transactionRequestRepository.save(transactionRequest);
    }

}
