package com.example.userandpaymentinfo.model;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Casopis {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "naziv", unique = true, nullable = false)
    private String naziv;

    @Column(name = "issn", unique = true, nullable = false)
    private String issn;

    @Column(name = "autorPlaca", nullable = false)
    private Boolean autorPlaca;

    @Column(name = "glavniUrednik", nullable = true)
    private Long glavniUrednik;

    @OneToMany(mappedBy = "casopis")
    private List<PodaciORacunu> podaciORacunima = new ArrayList<PodaciORacunu>();

    @OneToMany(mappedBy = "casopis")
    private List<Recenzent> listaRecenzenata = new ArrayList<Recenzent>();

    @OneToMany(mappedBy = "casopis")
    private List<Urednik> listaUrednika = new ArrayList<Urednik>();

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "casopis_oblast",
            joinColumns = @JoinColumn(name = "casopis_id"),
            inverseJoinColumns = @JoinColumn(name = "oblast_id")
    )
    private List<NaucnaOblast> naucneOblasti = new ArrayList<NaucnaOblast>();

    public List<PodaciORacunu> getPodaciORacunima() {
        return podaciORacunima;
    }

    public void setPodaciORacunima(List<PodaciORacunu> podaciORacunima) {
        this.podaciORacunima = podaciORacunima;
    }

    public List<Recenzent> getListaRecenzenata() {
        return listaRecenzenata;
    }

    public void setListaRecenzenata(List<Recenzent> listaRecenzenata) {
        this.listaRecenzenata = listaRecenzenata;
    }

    public List<Urednik> getListaUrednika() {
        return listaUrednika;
    }

    public void setListaUrednika(List<Urednik> listaUrednika) {
        this.listaUrednika = listaUrednika;
    }

    public List<NaucnaOblast> getNaucneOblasti() {
        return naucneOblasti;
    }

    public void setNaucneOblasti(List<NaucnaOblast> naucneOblasti) {
        this.naucneOblasti = naucneOblasti;
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


    public Casopis() {
    }
}
