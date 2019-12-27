package com.example.userandpaymentinfo.dto;

public class RedirectUrlDTO {
    private String redirectUrl;
    private String id;


    public RedirectUrlDTO() {
    }

    public RedirectUrlDTO(String redirectUrl, String id) {
        this.redirectUrl = redirectUrl;
        this.id = id;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
