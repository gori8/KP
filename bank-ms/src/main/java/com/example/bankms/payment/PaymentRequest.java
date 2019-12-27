package com.example.bankms.payment;

import java.math.BigDecimal;

public class PaymentRequest {

    private String casopisUuid;
    private BigDecimal amount;

    public String getCasopisUuid() {
        return casopisUuid;
    }

    public void setCasopisUuid(String casopisUuid) {
        this.casopisUuid = casopisUuid;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


    public PaymentRequest() {
    }
}
