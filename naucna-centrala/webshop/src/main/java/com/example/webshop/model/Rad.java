package com.example.webshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Rad {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String naslov;

    private String apstrakt;

    private String kljucneReci;

    private String sadrzaj;

    @ManyToOne(fetch = FetchType.EAGER)
    private NaucnaOblast naucnaOblast;

    @ManyToOne(fetch = FetchType.EAGER)
    private Korisnik autor;

    @ManyToOne(fetch = FetchType.EAGER)
    private Izdanje izdanje;

    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "radovi_recenzenti",
            joinColumns = @JoinColumn(name = "rad_id"),
            inverseJoinColumns = @JoinColumn(name = "recenzent_id"))
    private List<Korisnik> recenzenti = new ArrayList<>();

}
