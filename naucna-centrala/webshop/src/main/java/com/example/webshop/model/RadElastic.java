package com.example.webshop.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "naucni-radovi", type = "naucni-radovi")
public class RadElastic {

    @Id
    private String id;

    private String naslov;

    private String apstrakt;

    private String kljucneReci;

    private String sadrzaj;

    private String naucnaOblast;

    private String autor;

    private String nazivCasopsa;

}
