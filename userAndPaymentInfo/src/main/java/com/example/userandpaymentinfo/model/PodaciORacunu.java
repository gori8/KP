package com.example.userandpaymentinfo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class PodaciORacunu {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nacinPlacanja", unique = false, nullable = false)
    private String nacinPlacanja;

    @Column(name = "brojRacuna", unique = false, nullable = false)
    private String brojRacuna;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Casopis casopis;

    @Column(name = "cenaZaPretplatu", nullable = true)
    private int cenaZaPretplatu;

    public int getCenaZaPretplatu() {
        return cenaZaPretplatu;
    }

    public void setCenaZaPretplatu(int cenaZaPretplatu) {
        this.cenaZaPretplatu = cenaZaPretplatu;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNacinPlacanja() {
        return nacinPlacanja;
    }

    public void setNacinPlacanja (String nacinPlacanja) {
        this.nacinPlacanja = nacinPlacanja;
    }

    public String getBrojRacuna() {
        return brojRacuna;
    }

    public void setBrojRacuna(String brojRacuna) {
        this.brojRacuna = brojRacuna;
    }

    public Casopis getCasopis() {
        return casopis;
    }

    public void setCasopis(Casopis casopis) {
        this.casopis = casopis;
    }

    public PodaciORacunu() {
    }
}
