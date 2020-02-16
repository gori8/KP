package com.example.webshop.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class Pretplata {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    protected Korisnik pretplatnik;

    @ManyToOne(fetch = FetchType.EAGER)
    protected Plan plan;

    @Column(name = "datumIsticanja", unique = false, nullable = false)
    private Date datumIsticanja;
}
