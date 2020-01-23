package com.example.userandpaymentinfo.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateLinksDTO {
    private String naziv;
    private List<Long> naciniPlacanja;
    private BigDecimal amount;
    private String redirectUrl;
}
