package com.example.demo.dto;

public class PodaciORacunuDTO {

    private Long id;
    private String nacinPlacanja;
    private String brojRacuna;
    private Long casopisId;
    private int stanjeNaRacunu;

    public int getStanjeNaRacunu() {
        return stanjeNaRacunu;
    }

    public void setStanjeNaRacunu(int stanjeNaRacunu) {
        this.stanjeNaRacunu = stanjeNaRacunu;
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

    public void setNacinPlacanja(String nacinPlacanja) {
        this.nacinPlacanja = nacinPlacanja;
    }

    public String getBrojRacuna() {
        return brojRacuna;
    }

    public void setBrojRacuna(String brojRacuna) {
        this.brojRacuna = brojRacuna;
    }

    public Long getCasopisId() {
        return casopisId;
    }

    public void setCasopisId(Long casopisId) {
        this.casopisId = casopisId;
    }

    public PodaciORacunuDTO() {
    }
}
