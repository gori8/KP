package com.example.userandpaymentinfo.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class NacinPlacanja {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nacinPlacanja", unique = true, nullable = false)
    private String nacinPlacanja;

    @Column(name = "url", unique = true, nullable = false)
    private String url;

    @ManyToMany(mappedBy = "nacinPlacanjaList")
    private
    List<Casopis> casopisList = new ArrayList<>();

}
