package com.example.userandpaymentinfo.dto;



import com.example.userandpaymentinfo.model.NacinPlacanja;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class CasopisDTO {

    private Long id;
    private String naziv;
    private String uuid;
    private Long nacinPlacanjaId;
    private String redirectUrl;
    private BigDecimal amount;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getNacinPlacanjaId() {
        return nacinPlacanjaId;
    }

    public void setNacinPlacanjaId(Long nacinPlacanjaId) {
        this.nacinPlacanjaId = nacinPlacanjaId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }


}
