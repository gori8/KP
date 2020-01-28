package com.example.webshop.model;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
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
}
