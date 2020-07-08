package com.example.webshop.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class RadFoundDTO {
    private String id;
    private String sadrzaj;
    private String naslov;
    private String apstrakt;
    private String autor;
    private String kljucneReci;
    private String nazivCasopisa;
    private String naucnaOblast;
    private String sazetak;
    private String path;
}
