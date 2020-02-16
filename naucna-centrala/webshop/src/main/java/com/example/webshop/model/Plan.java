package com.example.webshop.model;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
public class Plan {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid", unique = true, nullable = true)
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID uuid;

    @Column(name = "period", unique = false, nullable = true)
    private String period;

    @Column(name = "ucestalostPerioda", unique = false, nullable = true)
    private Integer ucestalostPerioda;

    @Column(name = "cena", unique = false, nullable = true)
    private BigDecimal cena;

    @ManyToOne(fetch = FetchType.EAGER)
    private Casopis casopis;

    @OneToMany(mappedBy = "plan")
    protected List<Pretplata> pretplate = new ArrayList<>();
}
