package rs.ac.uns.ftn.paypal.utils;

import com.paypal.api.payments.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import rs.ac.uns.ftn.paypal.dto.AmountAndUrlDTO;
import rs.ac.uns.ftn.url.UrlClass;

import java.math.BigDecimal;
import java.util.UUID;

public class MyPaymentUtils {

    private static final String CURRENCY="USD";
    private static final String RETURN_URL = "https://github.com/" ;
    //private static final String CANCEL_URL = "http://localhost/paypal/cancel/" ;
    //private static final String AMOUNTANDURL= "http://localhost:8090/api/amountandurl/";


    static RestTemplate restTemplate=new RestTemplate();

    public static Transaction setTransaction(BigDecimal total, String paypalEmail) {
        Amount amount = setAmount(total);

        Payee payee = setPayee(paypalEmail);

        Transaction transaction = new Transaction();

        transaction.setAmount(amount);
        transaction.setPayee(payee);

        return transaction;
    }

    public static Amount setAmount(BigDecimal total) {
        Amount amount = new Amount();

        amount.setCurrency(CURRENCY);
        amount.setTotal(total.toString());

        return amount;
    }

    public static Payee setPayee(String email) {
        Payee payee = new Payee();

        payee.setEmail(email);

        return payee;
    }

    public static Payer generatePayer(String method){
        Payer payer=new Payer();
        payer.setPaymentMethod(method);
        return payer;
    }

    public static RedirectUrls setRedirectUrls(Long id) {
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setReturnUrl(RETURN_URL);
        redirectUrls.setCancelUrl(UrlClass.CANCEL_URL_PAYPAL + id.toString());
        return redirectUrls;
    }

    public static AmountAndUrlDTO getAmountAndRedirectUrl(String casopisID){
        String url=UrlClass.DOBAVI_CENU_URL_SA_PAYMENT_INFO+casopisID;
        System.out.println(url);
        ResponseEntity<AmountAndUrlDTO> resp
                = restTemplate.getForEntity(url, AmountAndUrlDTO.class);
        return resp.getBody();
    }

}
