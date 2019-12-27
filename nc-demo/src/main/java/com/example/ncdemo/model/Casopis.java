package com.example.ncdemo.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class Casopis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "naziv", unique = true, nullable = false)
    private String naziv;

    @Column(name = "userAndPaymentId", unique = true, nullable = false)
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID userAndPaymentId;

    @Column(name = "placen", unique = false, nullable = false)
    private Boolean placen;

    public Casopis(Long id, String naziv, UUID userAndPaymentId, Boolean placen) {
        this.id = id;
        this.naziv = naziv;
        this.userAndPaymentId = userAndPaymentId;
        this.placen = placen;
    }

    public Casopis() {
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

    public UUID getUserAndPaymentId() {
        return userAndPaymentId;
    }

    public void setUserAndPaymentId(UUID userAndPaymentId) {
        this.userAndPaymentId = userAndPaymentId;
    }

    public Boolean getPlacen() {
        return placen;
    }

    public void setPlacen(Boolean placen) {
        this.placen = placen;
    }
}
