package com.example.bankms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import javax.validation.constraints.Size;


@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "firstName", unique = false, nullable = false)
    private String firstName;

    @Column(name = "lastName", unique = false, nullable = false)
    private String lastName;

    @Size(max = 30)
    @Column(name = "merchantId", unique = false, nullable = false)
    private String merchantId;

    @Column(name = "casopisUuid", unique = false, nullable = false)
    private String casopisUuid;

    @Size(max = 100)
    @Column(name = "merchantPassword", unique = false, nullable = false)
    private String merchantPassword;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCasopisUuid() {
        return casopisUuid;
    }

    public void setCasopisUuid(String casopisUuid) {
        this.casopisUuid = casopisUuid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantPassword() {
        return merchantPassword;
    }

    public void setMerchantPassword(String merchantPassword) {
        this.merchantPassword = merchantPassword;
    }

    public Client() {
    }
}
