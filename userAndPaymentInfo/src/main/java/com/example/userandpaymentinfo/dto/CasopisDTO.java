package com.example.userandpaymentinfo.dto;



import com.example.userandpaymentinfo.model.NacinPlacanja;

import java.util.ArrayList;
import java.util.List;

public class CasopisDTO {

    private Long id;
    private String naziv;
    private String issn;
    private Long nacinPlacanjaId;

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

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }


    public CasopisDTO() {
    }

    public CasopisDTO(Long id, String naziv, String issn) {
        this.id = id;
        this.naziv = naziv;
        this.issn = issn;
    }
}
