package com.example.demo.model;


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

    @OneToMany(mappedBy = "casopis")
    private List<PodaciORacunu> podaciORacunima = new ArrayList<PodaciORacunu>();

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })

    public List<PodaciORacunu> getPodaciORacunima() {
        return podaciORacunima;
    }

    public void setPodaciORacunima(List<PodaciORacunu> podaciORacunima) {
        this.podaciORacunima = podaciORacunima;
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
