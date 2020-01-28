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

    @Column(name = "checkUrl", unique = true, nullable = false)
    private String checkUrl;

    @ManyToMany(mappedBy = "nacinPlacanjaList")
    private
    List<Item> itemList = new ArrayList<>();

}
