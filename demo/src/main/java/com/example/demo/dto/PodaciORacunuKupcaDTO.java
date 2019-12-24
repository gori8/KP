package com.example.demo.dto;

public class PodaciORacunuKupcaDTO {

    private Long id;
    private String nacinPlacanja;
    private String brojRacuna;
    private Long kupacId;
    private int stanjeNaRacunu;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNacinPlacanja() {
        return nacinPlacanja;
    }

    public void setNacinPlacanja(String nacinPlacanja) {
        this.nacinPlacanja = nacinPlacanja;
    }

    public String getBrojRacuna() {
        return brojRacuna;
    }

    public void setBrojRacuna(String brojRacuna) {
        this.brojRacuna = brojRacuna;
    }

    public Long getKupacId() {
        return kupacId;
    }

    public void setKupacId(Long kupacId) {
        this.kupacId = kupacId;
    }

    public int getStanjeNaRacunu() {
        return stanjeNaRacunu;
    }

    public void setStanjeNaRacunu(int stanjeNaRacunu) {
        this.stanjeNaRacunu = stanjeNaRacunu;
    }

    public PodaciORacunuKupcaDTO() {
    }
}
