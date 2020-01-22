package com.example.webshop.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ReturnLinksDTO {
    private List<LinkDTO> links = new ArrayList<>();
    private String uuid;
}
