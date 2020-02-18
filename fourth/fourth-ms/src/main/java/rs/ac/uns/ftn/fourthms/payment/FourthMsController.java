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

@RequestMapping("/api/fourth")
@RestController
@CrossOrigin("*")
public class FourthMsController {

    private final Logger LOGGER = LoggerFactory.getLogger(FourthMsController.class);


    @Autowired
    RestTemplate restTemplate;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> postPreparePayment(@RequestBody PreparePaymentRequest request) {

        LOGGER.info("Preparing payment on fourth micro service...");

        return new ResponseEntity<>("\""+"https://localhost:4600/payment/"+request.getCasopisUuid()+"\"", HttpStatus.OK);
    }

    @RequestMapping(value = "/execute", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody PaymentDTO dto) throws URISyntaxException {

        LOGGER.info("Executing payment on forth micro service...");

        String fooResourceUrl1
                = UrlClass.DOBAVI_CENU_URL_SA_PAYMENT_INFO + dto.getUuid();

        ResponseEntity<AmountAndUrlDTO> resp
                = restTemplate.getForEntity(fooResourceUrl1, AmountAndUrlDTO.class);

        LOGGER.info("Logging payment info:");
        LOGGER.info("Buyer: "+dto.getBuyerId());
        LOGGER.info("Seller: "+resp.getBody().getSellerEmail());
        LOGGER.info("Amount: "+resp.getBody().getAmount());
        LOGGER.info("Bought item UUID: "+resp.getBody().getItemUuid());

        LOGGER.info("Payment executed...");

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
