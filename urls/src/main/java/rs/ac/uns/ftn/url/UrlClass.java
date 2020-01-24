package rs.ac.uns.ftn.url;

public class UrlClass {

    public static final String DOBAVI_CENU_URL_SA_PAYMENT_INFO = "http://localhost:8090/api/amountandurl/";
    public static final String DOBAVI_REDIRECT_URL_SA_BANKE = "https://localhost:8091/payment";
    public static final String SUCCESS_URL_BTC = "http://localhost:8093/api/bitcoin/payment/success/";
    public static final String CANCEL_URL_BTC = "http://localhost:8093/api/bitcoin/payment/cancel/";
    public static final String CANCEL_URL_PAYPAL = "http://localhost/paypal/cancel/" ;
    public static final String DOBAVI_KP_FRONT_URL_SA_NACINIMA_PLACANJA_FROM_PAYMENT_INFO = "https://localhost:8771/userandpayment/api/url";//NC-DEMO
    public static final String DOBAVI_CASOPIS_NA_NC_DEMO = "https://localhost:9000/nc/casopis/";//NC-DEMO
    public static final String REDIRECT_URL_REGISTRATION = "https://localhost:9000/webshop/payments/complete/";
    public static final String USER_AND_PAYMENT_URL = "https://localhost:8771/userandpayment/api/";
    public static final String BANKMS_URL = "https://localhost:8771/bank/api/bankms/";


    //frontovi

    public static final String FRON_WEBSHOP = "https://localhost:4500/";
    public static final String FRONT_KP = "https://localhost:4200";
    public static final String FRONT_BANKE = "https://localhost:4300/";
    public static final String FRONT_PAYPAL = "https://localhost:4400/";
    public static final String FRONT_NC = "https://localhost:4500/casopis/";

    public static void main(String[] args) {
        System.out.println("hello");
    }
}
