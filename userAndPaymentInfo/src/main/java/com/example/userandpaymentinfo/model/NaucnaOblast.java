package com.example.userandpaymentinfo.model;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class NaucnaOblast {

    @Column(name = "naziv", unique = false, nullable = false)
    private String naziv;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(mappedBy = "naucneOblasti")
    private List<Recenzent> listaRecenzenata = new ArrayList<>();

    @ManyToMany(mappedBy = "naucneOblasti")
    private List<Urednik> listaUrednika = new ArrayList<>();

    @ManyToMany(mappedBy = "naucneOblasti")
    private List<Casopis> listaCasopisa = new ArrayList<>();

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

    public NaucnaOblast() {
    }
}
