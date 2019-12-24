package com.example.demo.dto;

public class KupovinaDTO {

    private Long id;
    private Long casopisId;
    private Long kupacId;
    private Long racunCasopisaId;
    private int iznos;
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

    public int getIznos() {
        return iznos;
    }

    public void setIznos(int iznos) {
        this.iznos = iznos;
    }

    public Long getRacunKupcaId() {
        return racunKupcaId;
    }

    public void setRacunKupcaId(Long racunKupcaId) {
        this.racunKupcaId = racunKupcaId;
    }

    public KupovinaDTO() {
    }
}
