package com.example.webshop.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ElementDTO {
    private String field;
    private String value;
    private String operator;
}
