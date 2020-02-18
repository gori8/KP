package com.example.userandpaymentinfo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RedUrl {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    @ManyToOne(fetch = FetchType.EAGER)
    private Item item;

    @Column(name = "uuid", unique = true, nullable = false)
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID uuid = UUID.randomUUID();

}
