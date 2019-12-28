package com.example.bankms.model;


import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount", unique = false, nullable = true)
    private BigDecimal amount;

    @Column(name = "url", unique = false, nullable = false)
    private String url;

    @Column(name = "casopisUuid", unique = false, nullable = false)
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID casopisUuid;

    @Column(name = "placeno", unique = false, nullable = false)
    private Boolean placeno;


}
