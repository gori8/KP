package com.example.webshop.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateLinksDTO {
    private String naziv;
    private List<Long> naciniPlacanja;
    private BigDecimal amount;
}
