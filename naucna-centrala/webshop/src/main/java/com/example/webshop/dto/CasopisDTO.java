package com.example.webshop.dto;

import com.example.webshop.model.Izdanje;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CasopisDTO {
    private Long id;
    private String naziv;
    private BigDecimal clanarina;
    private String komeSeNaplacuje;
    private Long issn;
    private Boolean aktiviran;
    private List<IzdanjeDTO> izdanja;
    private String urednik;
    private String uuid;
}
