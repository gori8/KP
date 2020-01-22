package com.example.webshop.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class NaucnaOblast {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ime", unique = true, nullable = false)
    private String naziv;

    @ManyToMany(mappedBy = "naucneOblasti")
    private List<Korisnik> korisnici = new ArrayList<>();

    @ManyToMany(mappedBy = "naucneOblasti")
    private List<Casopis> casopisi = new ArrayList<>();
}
