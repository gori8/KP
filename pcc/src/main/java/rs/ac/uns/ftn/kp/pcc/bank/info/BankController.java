package rs.ac.uns.ftn.kp.pcc.bank.info;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import rs.ac.uns.ftn.kp.pcc.bank.request.PccEntity;
import rs.ac.uns.ftn.kp.pcc.bank.request.PccEntityRepository;
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
    PccEntityRepository pccEntityRepository;

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


        PccEntity pccEntity = saveRequest(requestFromAcquirer);

        Bank bank = bankRepository.findOneByBankCode(pan);

        if(bank==null){
            pccEntity.setStatus(TransactionStatus.ERROR);
            pccEntityRepository.save(pccEntity);
            return ResponseEntity.ok(TransactionStatus.ERROR);
        }
        ResponseEntity<PccEntity> response = restTemplate.postForEntity(bank.getBankUrl(),requestFromAcquirer,PccEntity.class);
        pccEntity.setStatus(response.getBody().getStatus());
        pccEntity.setIssuerOrderId(response.getBody().getIssuerOrderId());
        pccEntity.setIssuerTimeStamp(response.getBody().getIssuerTimeStamp());
        pccEntityRepository.save(pccEntity);
        return ResponseEntity.ok(response.getBody().getStatus());
    }

    public PccEntity saveRequest(PccDTO requestFromAcquirer) throws ParseException {

        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");

        PccEntity transactionRequest = new PccEntity();
        transactionRequest.setAcquirerOrderId(Long.valueOf(requestFromAcquirer.getAcquirerOrderId()));
        transactionRequest.setAcquirerTimeStamp(requestFromAcquirer.getAcquirerTimestamp());
        transactionRequest.setAcquirerBankCode(requestFromAcquirer.getBankCode());
        transactionRequest.setAmount(requestFromAcquirer.getAmount());
        transactionRequest.setIssuerBankCode(requestFromAcquirer.getPan().substring(1,7));
        transactionRequest.setStatus(TransactionStatus.CREATED);

        return pccEntityRepository.save(transactionRequest);
    }

}
