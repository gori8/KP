package com.example.demo.model;

import javax.persistence.*;

@Entity
public class Kupovina {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "casopisId", unique = false, nullable = false)
    private Long casopisId;

    @Column(name = "kupacId", unique = false, nullable = false)
    private Long kupacId;

    @Column(name = "racunCasopisaId", unique = false, nullable = false)
    private Long racunCasopisaId;

    @Column(name = "iznos", unique = false, nullable = false)
    private double iznos;

    @Column(name = "racunKupcaId", unique = false, nullable = true)
    private Long racunKupcaId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCasopisId() {
        return casopisId;
    }

    public void setCasopisId(Long casopisId) {
        this.casopisId = casopisId;
    }

    public Long getKupacId() {
        return kupacId;
    }

    public void setKupacId(Long kupacId) {
        this.kupacId = kupacId;
    }

    public Long getRacunCasopisaId() {
        return racunCasopisaId;
    }

    public void setRacunCasopisaId(Long racunCasopisaId) {
        this.racunCasopisaId = racunCasopisaId;
    }

    public double getIznos() {
        return iznos;
    }

    public void setIznos(double iznos) {
        this.iznos = iznos;
    }

    public Long getRacunKupcaId() {
        return racunKupcaId;
    }

    public void setRacunKupcaId(Long racunKupcaId) {
        this.racunKupcaId = racunKupcaId;
    }

    public Kupovina() {
    }
}
