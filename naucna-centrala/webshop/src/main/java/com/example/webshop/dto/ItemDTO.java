package com.example.webshop.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ItemDTO {
    private String naziv;
    private List<Long> naciniPlacanja;
    private BigDecimal amount;
    private String redirectUrl;
    private String email;
}
