package com.example.userandpaymentinfo.dto;

public class NacinPlacanjaDTO {
    private Long value;
    private String name;
    private String url;

    public NacinPlacanjaDTO() {
    }

    public NacinPlacanjaDTO(Long value, String name, String url) {
        this.value = value;
        this.name = name;
        this.url = url;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
