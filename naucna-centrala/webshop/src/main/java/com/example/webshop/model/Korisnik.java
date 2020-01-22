package com.example.webshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Data
public class Korisnik implements UserDetails {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ime", unique = false, nullable = false)
    private String ime;

    @Column(name = "prezime", unique = false, nullable = false)
    private String prezime;

    @Column(name = "grad", unique = false, nullable = false)
    private String grad;

    @Column(name = "drzava", unique = false, nullable = false)
    private String drzava;

    @Column(name = "titula", unique = false, nullable = true)
    private String titula;

    @Column(name = "email", unique = false, nullable = false)
    private String email;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", unique = false, nullable = false)
    private String password;

    @Column(name = "recenzent", unique = false)
    private Boolean recenzent;

    @Column(name = "aktiviran", unique = false)
    private Boolean aktiviran = false;

    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "korisnik_naucna_oblast",
            joinColumns = @JoinColumn(name = "korisnik_id"),
            inverseJoinColumns = @JoinColumn(name = "naucna_oblast_id"))
    private List<NaucnaOblast> naucneOblasti = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_authority",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"))
    private List<Authority> authorities;

    @OneToMany(mappedBy = "glavniUrednik")
    protected List<Casopis> casopisiGlavni = new ArrayList<>();

    @ManyToMany(mappedBy = "urednici")
    private List<Casopis> casopisiUrednik = new ArrayList<>();

    @ManyToMany(mappedBy = "recenzenti")
    private List<Casopis> casopisiRecenzent = new ArrayList<>();


    @Override
    public boolean isEnabled() {
        return aktiviran;
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }


    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

}
