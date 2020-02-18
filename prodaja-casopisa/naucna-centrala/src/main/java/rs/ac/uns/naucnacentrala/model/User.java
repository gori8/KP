//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.05.23 at 02:20:55 PM CEST 
//


package rs.ac.uns.naucnacentrala.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.*;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", unique = false, nullable = false)
    private String password;

    @Column(name = "ime", unique = false, nullable = false)
    private String ime;

    @Column(name = "prezime", unique = false, nullable = false)
    private String prezime;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "enabled")
    private boolean enabled;

    private String grad;

    private String drzava;

    private String titula;

    @Column(name = "last_password_reset_date")
    private Timestamp lastSifraResetDate;

    @ManyToMany(cascade = {
            CascadeType.MERGE,
            CascadeType.REFRESH
    })
    @JoinTable(name = "user_naucneoblasti",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "naucnaoblast_id", referencedColumnName = "id"))
    private List<NaucnaOblast> naucneOblasti=new ArrayList<>();


    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_authority",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"))
    private List<Authority> authorities;


    @ManyToMany(mappedBy = "recezenti",cascade = {
            CascadeType.MERGE,
            CascadeType.PERSIST
    }, fetch = FetchType.LAZY)
    private List<Casopis> recenziramCasopise=new ArrayList<Casopis>();

    @ManyToMany(mappedBy = "urednici",cascade = {
            CascadeType.MERGE,
            CascadeType.PERSIST
    }, fetch = FetchType.LAZY)
    private List<Casopis> uredjujemCasopise=new ArrayList<>();


    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_casopis",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "casopis_id", referencedColumnName = "id"))
    private List<Casopis> kupljeniCasopisi = new ArrayList<>();



    @Override
    public boolean isEnabled() {
        return enabled;
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
