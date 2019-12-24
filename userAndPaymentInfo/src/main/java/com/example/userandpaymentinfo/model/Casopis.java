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

    @Column(name = "autorPlaca", nullable = true)
    private Boolean autorPlaca;

    @ManyToMany
    @JoinTable(
            name = "casopic_nacin_placanja",
            joinColumns = @JoinColumn(name = "casopis_id"),
            inverseJoinColumns = @JoinColumn(name = "nacin_placanja_id"))
    private List<NacinPlacanja> nacinPlacanjaList = new ArrayList<>();

    public List<NacinPlacanja> getNacinPlacanjaList() {
        return nacinPlacanjaList;
    }

    public void setNacinPlacanjaList(List<NacinPlacanja> nacinPlacanjaList) {
        this.nacinPlacanjaList = nacinPlacanjaList;
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


    public Casopis() {
    }
}
