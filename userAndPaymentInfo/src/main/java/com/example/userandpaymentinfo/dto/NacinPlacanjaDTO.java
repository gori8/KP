package com.example.userandpaymentinfo.dto;

public class NacinPlacanjaDTO {
    private Long value;
    private String name;

    public NacinPlacanjaDTO() {
    }

    public NacinPlacanjaDTO(Long value, String name) {
        this.value = value;
        this.name = name;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
