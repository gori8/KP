package com.example.ncdemo.dto;

import java.util.UUID;

public class CasopisDTO {
    private String userAndPaymentId;
    private String naziv;
    private Boolean placen;

    public CasopisDTO(String userAndPaymentId, String naziv, Boolean placen) {
        this.userAndPaymentId = userAndPaymentId;
        this.naziv = naziv;
        this.placen = placen;
    }

    public CasopisDTO() {
    }

    public String getUserAndPaymentId() {
        return userAndPaymentId;
    }

    public void setUserAndPaymentId(String userAndPaymentId) {
        this.userAndPaymentId = userAndPaymentId;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public Boolean getPlacen() {
        return placen;
    }

    public void setPlacen(Boolean placen) {
        this.placen = placen;
    }
}
