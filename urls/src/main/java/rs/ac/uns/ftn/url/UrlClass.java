package rs.ac.uns.ftn.url;

public class UrlClass {

    public static final String DOBAVI_CENU_URL_SA_PAYMENT_INFO = "http://localhost:8090/api/amountandurl/";
    public static final String DOBAVI_REDIRECT_URL_SA_BANKE = "https://localhost:8091/payment";
    public static final String SUCCESS_URL_BTC = "http://localhost:8093/api/bitcoin/payment/success/";
    public static final String CANCEL_URL_BTC = "http://localhost:8093/api/bitcoin/payment/cancel/";
    public static final String CANCEL_URL_PAYPAL = "http://localhost/paypal/cancel/" ;
    public static final String DOBAVI_KP_FRONT_URL_SA_NACINIMA_PLACANJA_FROM_PAYMENT_INFO = "http://localhost:8771/userandpayment/api/url";//NC-DEMO
    public static final String DOBAVI_CASOPIS_NA_NC_DEMO = "http://localhost:9000/nc/casopis/";//NC-DEMO



    //frontovi
<<<<<<< HEAD
    public static final String FRONT_KP = "http://localhost:4200";
    public static final String FRONT_BANKE = "http://localhost:4300/";
    public static final String FRONT_PAYPAL = "http://localhost:4400/";
    public static final String FRONT_NC = "http://localhost:4500/casopis/";
=======
    public static final String FRONT_KP = "https://localhost:4200";
    public static final String FRONT_BANKE = "https://localhost:4300/";
    public static final String FRONT_PAYPAL = "https://localhost:4400/";
    public static final String FRONT_NC = "https://localhost:4500/casopis/";
>>>>>>> 8d51fd335e74ee0b8e8180ee3916e9322f73a99c

    public static void main(String[] args) {
        System.out.println("hello");
    }
}
