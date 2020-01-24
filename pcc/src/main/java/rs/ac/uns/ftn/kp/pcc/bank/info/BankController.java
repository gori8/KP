package rs.ac.uns.ftn.kp.pcc.bank.info;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.ftn.kp.pcc.bank.request.TransactionRequest;
import rs.ac.uns.ftn.kp.pcc.bank.request.TransactionRequestRepository;
import rs.ac.uns.ftn.kp.pcc.dto.RegisterBankDTO;
import rs.ac.uns.ftn.kp.pcc.dto.RequestFromAcquirer;
import rs.ac.uns.ftn.kp.pcc.dto.ResponseToIssuer;

@RestController
@RequestMapping("/api/pcc")
//@CrossOrigin(origins = "*")
public class BankController {

    @Autowired
    BankRepository bankRepository;

    @Autowired
    TransactionRequestRepository transactionRequestRepository;


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<Bank> registerBank(@RequestBody RegisterBankDTO registrationDTO) {

        Bank bank = new Bank();
        bank.setBankCode(registrationDTO.getBankCode());
        bank.setBankUrl(registrationDTO.getBankUrl());
        Bank ret = bankRepository.save(bank);

        return new ResponseEntity<Bank>(ret, HttpStatus.OK);
    }

    @RequestMapping(value = "/response", method = RequestMethod.POST)
    public ResponseEntity<ResponseToIssuer> makeResponse(@RequestBody RequestFromAcquirer requestFromAcquirer) {

        ResponseToIssuer responseToIssuer = new ResponseToIssuer();
        String pan = requestFromAcquirer.getPan();
        pan = pan.substring(1, 7);
        System.out.println(pan);

        Bank bank = bankRepository.findOneByBankCode(pan);

        responseToIssuer.setBankUrl(bank.getBankUrl());

        saveRequest(requestFromAcquirer);

        return new ResponseEntity<ResponseToIssuer>(responseToIssuer, HttpStatus.OK);
    }

    public void saveRequest(RequestFromAcquirer requestFromAcquirer) {

        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setAcquirerOrderId(requestFromAcquirer.getAcquirerOrderId());
        transactionRequest.setAcquirerTimestamp(requestFromAcquirer.getAcquirerTimestamp());
        transactionRequest.setHolderName(requestFromAcquirer.getHolderName());
        transactionRequest.setPan(requestFromAcquirer.getPan());
        transactionRequest.setSecurityCode(requestFromAcquirer.getSecurityCode());
        transactionRequest.setValidTo(requestFromAcquirer.getValidTo());

        transactionRequestRepository.save(transactionRequest);
    }

}
