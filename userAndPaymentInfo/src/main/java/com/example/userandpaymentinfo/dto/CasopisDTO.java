package com.example.userandpaymentinfo.dto;



import java.util.ArrayList;
import java.util.List;

public class CasopisDTO {

    private Long id;
    private String naziv;
    private String issn;
    private Boolean autorPlaca;
    private Long glavniUrednik;

    private List<NaucnaOblastDTO> naucneOblasti = new ArrayList<NaucnaOblastDTO>();
    private List<PodaciORacunuDTO> podaciORacunima = new ArrayList<PodaciORacunuDTO>();
    private List<RecenzentDTO> listaRecenzenata = new ArrayList<RecenzentDTO>();
    private List<UrednikDTO> listaUrednika = new ArrayList<UrednikDTO>();

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

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public Boolean getAutorPlaca() {
        return autorPlaca;
    }

    public void setAutorPlaca(Boolean autorPlaca) {
        this.autorPlaca = autorPlaca;
    }

    public Long getGlavniUrednik() {
        return glavniUrednik;
    }

    public void setGlavniUrednik(Long glavniUrednik) {
        this.glavniUrednik = glavniUrednik;
    }

    public List<NaucnaOblastDTO> getListaNaucnihOblasti() {
        return naucneOblasti;
    }

    public void setListaNaucnihOblasti(List<NaucnaOblastDTO> listaNaucnihOblasti) {
        this.naucneOblasti = listaNaucnihOblasti;
    }

    public List<PodaciORacunuDTO> getListaPodatakaORacunima() {
        return podaciORacunima;
    }

    public void setListaPodatakaORacunima(List<PodaciORacunuDTO> listaPodatakaORacunima) {
        this.podaciORacunima = listaPodatakaORacunima;
    }

    public List<RecenzentDTO> getListaRecenzenata() {
        return listaRecenzenata;
    }

    public void setListaRecenzenata(List<RecenzentDTO> listaRecenzenata) {
        this.listaRecenzenata = listaRecenzenata;
    }

    public List<UrednikDTO> getListaUrednika() {
        return listaUrednika;
    }

    public void setListaUrednika(List<UrednikDTO> listaUrednika) {
        this.listaUrednika = listaUrednika;
    }

    public CasopisDTO() {
    }
}
