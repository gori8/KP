package com.example.webshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class Casopis {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid", unique = true, nullable = true)
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID uuid;

    @Column(name = "naziv", unique = false, nullable = false)
    private String naziv;

    @Column(name = "clanarina", unique = false, nullable = false)
    private BigDecimal clanarina;

    @Column(name = "komeSeNaplacuje", unique = false, nullable = false)
    private String komeSeNaplacuje;

    @Column(name = "issn", unique = true, nullable = false)
    private Long issn;

    @Column(name = "aktiviran", unique = false, nullable = false)
    private Boolean aktiviran;

    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "casopis_naucna_oblast",
            joinColumns = @JoinColumn(name = "casopis_id"),
            inverseJoinColumns = @JoinColumn(name = "naucna_oblast_id"))
    private List<NaucnaOblast> naucneOblasti = new ArrayList<>();

    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "casopis_nacin_placanja",
            joinColumns = @JoinColumn(name = "casopis_id"),
            inverseJoinColumns = @JoinColumn(name = "nacin_placanja_id"))
    private List<NacinPlacanja> naciniPlacanja = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    protected Korisnik glavniUrednik;

    @OneToMany(mappedBy = "casopis")
    protected List<Link> linkovi = new ArrayList<>();

    @OneToMany(mappedBy = "casopis")
    protected List<Izdanje> izdanja = new ArrayList<>();

    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "casopis_urednik",
            joinColumns = @JoinColumn(name = "casopis_id"),
            inverseJoinColumns = @JoinColumn(name = "urednik_id"))
    private List<Korisnik> urednici = new ArrayList<>();

    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "casopis_recenzent",
            joinColumns = @JoinColumn(name = "casopis_id"),
            inverseJoinColumns = @JoinColumn(name = "recenzent_id"))
    private List<Korisnik> recenzenti = new ArrayList<>();

    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "casopis_pretplatnik",
            joinColumns = @JoinColumn(name = "casopis_id"),
            inverseJoinColumns = @JoinColumn(name = "pretplatnik_id"))
    private List<Korisnik> pretplatnici = new ArrayList<>();
}
