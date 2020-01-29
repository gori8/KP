package com.example.webshop.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class IzdanjeDTO {

    private Long id;
    private String naziv;
    private Long broj;
    private BigDecimal cena;
    private Long casopisId;
    private Date datumIzdanja;
    private String uuid;
}
