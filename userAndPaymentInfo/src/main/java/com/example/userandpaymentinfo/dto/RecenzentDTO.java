package com.example.userandpaymentinfo.dto;

import com.example.userandpaymentinfo.model.Titula;

import java.util.ArrayList;
import java.util.List;

public class RecenzentDTO {

    private Long id;
    private String username;
    private String password;
    private String ime;
    private String prezime;
    private Titula titula;
    private String grad;
    private String drzava;
    private String email;
    private Boolean odobren;
    private Long casopisId;

    private List<NaucnaOblastDTO> listaNaucnihOblasti = new ArrayList<NaucnaOblastDTO>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public Titula getTitula() {
        return titula;
    }

    public void setTitula(Titula titula) {
        this.titula = titula;
    }

    public String getGrad() {
        return grad;
    }

    public void setGrad(String grad) {
        this.grad = grad;
    }

    public String getDrzava() {
        return drzava;
    }

    public void setDrzava(String drzava) {
        this.drzava = drzava;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getOdobren() {
        return odobren;
    }

    public void setOdobren(Boolean odobren) {
        this.odobren = odobren;
    }

    public Long getCasopisId() {
        return casopisId;
    }

    public void setCasopisId(Long casopisId) {
        this.casopisId = casopisId;
    }

    public List<NaucnaOblastDTO> getListaNaucnihOblasti() {
        return listaNaucnihOblasti;
    }

    public void setListaNaucnihOblasti(List<NaucnaOblastDTO> listaNaucnihOblasti) {
        this.listaNaucnihOblasti = listaNaucnihOblasti;
    }

    public RecenzentDTO() {
    }
}
