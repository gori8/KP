package com.example.webshop.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CasopisDTO {
    private Long id;
    private String naziv;
    private BigDecimal clanarina;
    private String komeSeNaplacuje;
    private Long issn;
    private Boolean aktiviran;
}
