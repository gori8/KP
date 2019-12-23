package com.example.userandpaymentinfo.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
public class Recenzent {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", unique = false, nullable = false)
    private String password;

    @Column(name = "ime", unique = false)
    private String ime;

    @Column(name = "prezime", unique = false)
    private String prezime;

    @Column(name = "titula", unique = false, nullable = false)
    @Enumerated(EnumType.STRING)
    private Titula titula;

    @Column(name = "grad", unique = false)
    private String grad;

    @Column(name = "drzava", unique = false)
    private String drzava;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "odobren", unique = false)
    private Boolean odobren;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "recenzent_oblast",
            joinColumns = @JoinColumn(name = "recenzent_id"),
            inverseJoinColumns = @JoinColumn(name = "oblast_id")
    )
    private List<NaucnaOblast> naucneOblasti;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Casopis casopis;

    public Casopis getCasopis() {
        return casopis;
    }

    @JsonIgnore
    public void setCasopis(Casopis casopis) {
        this.casopis = casopis;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public Titula getTitula() {
        return titula;
    }

    public void setTitula(Titula titula) {
        this.titula = titula;
    }

    public String getGrad() {
        return grad;
    }

    public void setGrad(String grad) {
        this.grad = grad;
    }

    public String getDrzava() {
        return drzava;
    }

    public void setDrzava(String drzava) {
        this.drzava = drzava;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getOdobren() {
        return odobren;
    }

    public void setOdobren(Boolean odobren) {
        this.odobren = odobren;
    }

    public List<NaucnaOblast> getNaucneOblasti() {
        return naucneOblasti;
    }

    public void setNaucneOblasti(List<NaucnaOblast> naucneOblasti) {
        this.naucneOblasti = naucneOblasti;
    }

    public Recenzent() {
    }
}
