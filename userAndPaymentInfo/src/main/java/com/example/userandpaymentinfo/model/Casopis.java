package com.example.userandpaymentinfo.model;


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

    private BigDecimal amount;
}
