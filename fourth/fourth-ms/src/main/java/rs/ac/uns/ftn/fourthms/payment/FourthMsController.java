package rs.ac.uns.ftn.fourthms.payment;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import rs.ac.uns.ftn.fourthms.dto.PaymentDTO;
import rs.ac.uns.ftn.fourthms.dto.PreparePaymentRequest;
import rs.ac.uns.ftn.url.AmountAndUrlDTO;
import rs.ac.uns.ftn.url.UrlClass;

import java.net.URISyntaxException;
import java.time.LocalDateTime;

@RequestMapping("/api/fourth")
@RestController
@CrossOrigin("*")
public class FourthMsController {

    private final Logger LOGGER = LoggerFactory.getLogger(FourthMsController.class);

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> postPreparePayment(@RequestBody PreparePaymentRequest request) {

        LOGGER.info(LocalDateTime.now() + "      Preparing payment on fourth microservice...");

        return new ResponseEntity<>("\""+"https://localhost:4600/payment/"+request.getCasopisUuid()+"\"", HttpStatus.OK);
    }

    @RequestMapping(value = "/execute", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody PaymentDTO dto) throws URISyntaxException {

        LOGGER.info(LocalDateTime.now() + "     Executing payment on forth microservice...");

        String fooResourceUrl1
                = UrlClass.DOBAVI_CENU_URL_SA_PAYMENT_INFO + dto.getUuid();

        ResponseEntity<AmountAndUrlDTO> resp
                = restTemplate.getForEntity(fooResourceUrl1, AmountAndUrlDTO.class);

        LOGGER.info(LocalDateTime.now() + "     Payment info for fourth microservice:  " + "Buyer:  "+dto.getBuyerId() + "   Seller: "+resp.getBody().getSellerEmail() + "   Amount: "+resp.getBody().getAmount() + "   Item:   " + resp.getBody().getItemUuid());
        LOGGER.info(LocalDateTime.now() + "     Payment executed on fourth microservice...");

        return new  ResponseEntity<String>("\""+notifyNc(resp.getBody().getRedirectUrl()+"/true")+"\"",HttpStatus.OK);
    }

    private String notifyNc(String url){
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity=new HttpEntity("",headers);
        ResponseEntity<String> redirectUrl=restTemplate.postForEntity(url,entity,String.class);
        return redirectUrl.getBody();
    }
}
