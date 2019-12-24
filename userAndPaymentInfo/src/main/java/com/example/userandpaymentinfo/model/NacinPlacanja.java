package com.example.userandpaymentinfo.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class NacinPlacanja {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nacinPlacanja", unique = true, nullable = false)
    private String nacinPlacanja;

    @ManyToMany(mappedBy = "nacinPlacanjaList")
    private
    List<Casopis> casopisList = new ArrayList<>();

    public List<Casopis> getCasopisList() {
        return casopisList;
    }

    public void setCasopisList(List<Casopis> casopisList) {
        this.casopisList = casopisList;
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

    public NacinPlacanja() {
    }
}
