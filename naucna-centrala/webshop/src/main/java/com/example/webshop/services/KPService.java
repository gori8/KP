package com.example.webshop.services;

import com.example.webshop.model.Casopis;
import org.springframework.http.ResponseEntity;

public interface KPService {

    public void createLinks(Long casopisId);

    public String completePayment(String uuid, Long nacinPlacanjaId);
}
