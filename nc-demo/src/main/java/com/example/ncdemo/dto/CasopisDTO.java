package com.example.ncdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CasopisDTO {

    private String userAndPaymentId;
    private String naziv;
    private Boolean placen;

}
