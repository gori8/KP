package rs.ac.uns.ftn.kp.bankms.payment;

import java.math.BigDecimal;

public class PaymentRequest {

    private String casopisUuid;

    public String getCasopisUuid() {
        return casopisUuid;
    }

    public void setCasopisUuid(String casopisUuid) {
        this.casopisUuid = casopisUuid;
    }

    public PaymentRequest() {
    }
}
