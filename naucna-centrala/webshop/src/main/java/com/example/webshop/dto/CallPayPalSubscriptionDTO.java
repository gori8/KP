package com.example.webshop.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CallPayPalSubscriptionDTO {

    private String period;

    private Integer ucestalostPerioda;

    private BigDecimal cena;

    private String username;

    private String uuid;
}
