package com.example.ncdemo.model;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
public class Casopis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "naziv", unique = true, nullable = false)
    private String naziv;

    @Column(name = "userAndPaymentId", unique = true, nullable = false)
    @Type(type="org.hibernate.type.UUIDBinaryType")
    private UUID userAndPaymentId;

    @Column(name = "placen", unique = false, nullable = false)
    private Boolean placen;


}
