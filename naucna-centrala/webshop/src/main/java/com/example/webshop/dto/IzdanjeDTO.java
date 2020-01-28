package com.example.webshop.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class IzdanjeDTO {

    private Long id;
    private String naziv;
    private Long broj;
    private BigDecimal cena;
    private Long casopisId;
}
