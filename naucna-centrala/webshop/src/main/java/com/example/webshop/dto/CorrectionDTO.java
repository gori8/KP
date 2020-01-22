package com.example.webshop.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class CorrectionDTO {
    private Long id;
    private String naziv;
    private BigDecimal clanarina;
    private String komeSeNaplacuje;
    private List<Long> naucneOblasti = new ArrayList<>();
    private List<Long> naciniPlacanja = new ArrayList<>();
}
