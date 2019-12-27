package com.example.userandpaymentinfo.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    @Column(name = "uuid", unique = true, nullable = false)
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID uuid;

    @Column(name = "redirectUrl", unique = false, nullable = false)
    private String redirectUrl;

    @ManyToMany
    @JsonIgnore
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

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
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

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public Casopis() {
    }
}
