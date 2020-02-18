package rs.ac.uns.ftn.paypal.cmrs;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.paypal.api.payments.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.paypal.dto.*;
import rs.ac.uns.ftn.url.ObjectMapperUtils;
import rs.ac.uns.ftn.url.PayPalSubscriptionDTO;
import rs.ac.uns.ftn.url.UrlClass;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.DrbgParameters;
import java.text.ParseException;
import java.util.UUID;

@RequestMapping("/api/paypal")
@RestController
@CrossOrigin("*")
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @Autowired
    SellerRepository sellerRepository;

    @Autowired
    SubPlanRepository subPlanRepository;

    @PostMapping
    public String create(@RequestBody CreatePaymentOrSubRequest request) {
        System.out.println("Item: "+request.getCasopisUuid());
        return "\""+paymentService.createPayment(request).getApprovalUrl()+"\"";
    }

    @RequestMapping(value = "/execute", method = RequestMethod.GET)
    public ResponseEntity create(@RequestParam("paymentId")String paymentId,@RequestParam("token")String token,@RequestParam("PayerID")String payerId ) throws URISyntaxException {
        ExecutePaymentRequest request = new ExecutePaymentRequest(paymentId,payerId,token);
        URI redirectUrl = new URI(paymentService.executePayment(request));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(redirectUrl);
        return ResponseEntity.status(HttpStatus.FOUND).headers(httpHeaders).build();
    }

    @GetMapping("/cancel/{id}")
    public ResponseEntity cancelPayment(@PathVariable("id") Long id) throws URISyntaxException {
        URI redirectUrl = new URI(paymentService.cancelPayment(id));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(redirectUrl);
        return ResponseEntity.status(HttpStatus.FOUND).headers(httpHeaders).build();
    }

    @RequestMapping(value = "/plan", method = RequestMethod.POST)
    public ResponseEntity<String> createPlan(@RequestBody PayPalSubscriptionDTO request) {
        return new ResponseEntity<String>(paymentService.createSubPlanUrl(request), HttpStatus.OK);
    }

    @RequestMapping(value = "/subscription", method = RequestMethod.POST)
    public ResponseEntity<String> createSubscription(@RequestBody SubPlanDTO request) {
        return new ResponseEntity<String>("\""+paymentService.activateSubscription(request)+"\"", HttpStatus.OK);
    }

    @RequestMapping(value = "/subscription/confirm/{id}")
    public ResponseEntity confirmSubscription(@PathVariable String id,@RequestParam("token")String token) throws URISyntaxException {
        String url = paymentService.executeSubAgreement(Long.parseLong(id),token);
        URI redirectUrl = new URI(url);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(redirectUrl);
        return ResponseEntity.status(HttpStatus.FOUND).headers(httpHeaders).build();
    }

    @RequestMapping(value = "/subscription/cancel/{id}")
    public ResponseEntity cancelSubscription(@PathVariable String id,@RequestParam("token")String token) throws URISyntaxException {
        paymentService.rejectSubscription(Long.parseLong(id));
        URI redirectUrl = new URI("https://localhost:4500");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(redirectUrl);
        return ResponseEntity.status(HttpStatus.FOUND).headers(httpHeaders).build();
    }

    @RequestMapping(value = "/plan/{id}")
    public ResponseEntity getPlanViewData(@PathVariable("id") String id){
        SubPlan subPlan = subPlanRepository.findOneByPlanId(UUID.fromString(id));
        return ResponseEntity.ok(ObjectMapperUtils.map(subPlan,ViewPlanDTO.class));
    }


    @Autowired
    ObjectMapper objectMapper;

    @RequestMapping(value = "/paypal-webhooks", method = RequestMethod.POST)
    public ResponseEntity webhook(@RequestBody String eventJsonStr) throws JsonProcessingException, ParseException {
        System.out.println(eventJsonStr);
        JsonNode jsonNode = objectMapper.readTree(eventJsonStr);
        JsonNode resourceNode = jsonNode.get("resource");
        JsonNode agreementDetailsNode = resourceNode.get("agreement_details");
        JsonNode nextBillingDateNode = agreementDetailsNode.get("next_billing_date");
        JsonNode eventTypeNode = jsonNode.get("event_type");
        String eventType = objectMapper.treeToValue(eventTypeNode,String.class);
        System.out.println("Event type: "+eventType);
        if(eventType.equals("BILLING.SUBSCRIPTION.CANCELLED")) {
            Agreement agreement = objectMapper.treeToValue(resourceNode, Agreement.class);
            String nextBillingDate = objectMapper.treeToValue(nextBillingDateNode, String.class);
            paymentService.cancelSubscription(agreement,nextBillingDate);
            return ResponseEntity.status(200).build();
        }else if(eventType.equals("BILLING.SUBSCRIPTION.PAYMENT.FAILED")){
            return ResponseEntity.status(200).build();
        }else{
            return  ResponseEntity.status(422).build();
        }
    }

}
