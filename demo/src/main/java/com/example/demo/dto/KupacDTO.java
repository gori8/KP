package com.example.demo.dto;

import com.example.demo.model.Titula;

public class KupacDTO {

    private Long id;
    private String username;
    private String password;
    private String ime;
    private String prezime;
    private Titula titula;
    private String grad;
    private String drzava;
    private String email;
    private int btc;
    private int dinari;
    private int paypal;
    private int cetvrto;

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

    public int getBtc() {
        return btc;
    }

    public void setBtc(int btc) {
        this.btc = btc;
    }

    public int getDinari() {
        return dinari;
    }

    public void setDinari(int dinari) {
        this.dinari = dinari;
    }

    public int getPaypal() {
        return paypal;
    }

    public void setPaypal(int paypal) {
        this.paypal = paypal;
    }

    public int getCetvrto() {
        return cetvrto;
    }

    public void setCetvrto(int cetvrto) {
        this.cetvrto = cetvrto;
    }

    public KupacDTO() {
    }
}
