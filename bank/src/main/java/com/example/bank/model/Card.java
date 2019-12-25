package com.example.bank.model;

import javax.persistence.*;
import java.sql.Date;

@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pan", unique = false, nullable = false)
    private String pan;

    @Column(name = "securityCode", unique = false, nullable = false)
    private Integer securityCode;

    @Column(name = "holderName", unique = false, nullable = false)
    private String holderName;

    @Column(name = "validTo", unique = false, nullable = false)
    private Date validTo;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Account account;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public Integer getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(Integer securityCode) {
        this.securityCode = securityCode;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public Date getValidTo() {
        return validTo;
    }

    public void setValidTo(Date validTo) {
        this.validTo = validTo;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Card() {
    }
}
