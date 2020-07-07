package com.example.webshop.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class RadDTO {

    private String naslov;
    private String kljucneReci;
    private String apstrakt;
    private Long naucnaOblast;
    private String sadrzaj;
    private Long izdanjeId;

}
