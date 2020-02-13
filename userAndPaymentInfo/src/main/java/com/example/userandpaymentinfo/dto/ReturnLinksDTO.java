package com.example.userandpaymentinfo.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ReturnLinksDTO {
    private List<LinkDTO> links = new ArrayList<>();
    private String uuid;
    private String registerUrl;
}
