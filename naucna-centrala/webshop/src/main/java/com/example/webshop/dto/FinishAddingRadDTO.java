package com.example.webshop.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class FinishAddingRadDTO {
    private Long radId;
    private List<Long> recIds;
}
