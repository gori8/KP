package com.example.webshop.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UredniciRecenzentiDTO implements Serializable {
    List<String> urednici;
    List<String> recenzenti;
}
