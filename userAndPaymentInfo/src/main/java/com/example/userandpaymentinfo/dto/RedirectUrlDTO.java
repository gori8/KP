package com.example.userandpaymentinfo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RedirectUrlDTO {

    private String redirectUrl;
    private String id;
    private String checkStatusUrl;


}
