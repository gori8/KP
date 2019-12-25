package com.example.bankms.payment;

import java.math.BigDecimal;

public class PaymentRequest {

    private Long casopisId;
    private BigDecimal amount;

    public Long getCasopisId() {
        return casopisId;
    }

    public void setCasopisId(Long casopisId) {
        this.casopisId = casopisId;
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
