package com.example.bank.payment;

public class ExternalBankPaymentResponse {

    private Long id;
    private String url;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ExternalBankPaymentResponse() {
    }
}
