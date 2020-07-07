package com.example.webshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class Izdanje {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid", unique = true, nullable = true)
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID uuid;

    @Column(name = "naziv", unique = false, nullable = false)
    private String naziv;

    @Column(name = "broj", unique = false, nullable = false)
    private Long broj;

    @Column(name = "cena", unique = false, nullable = false)
    private BigDecimal cena;

    @Column(name = "datumIzdanja", unique = false, nullable = false)
    private Date datumIzdanja;

    @ManyToOne(fetch = FetchType.EAGER)
    protected Casopis casopis;

    @OneToMany(mappedBy = "izdanje")
    private List<Rad> radovi = new ArrayList<>();

    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "izdanje_kupac",
            joinColumns = @JoinColumn(name = "izdanje_id"),
            inverseJoinColumns = @JoinColumn(name = "kupac_id"))
    private List<Korisnik> kupci = new ArrayList<>();
}
