package com.example.webshop.services;

import com.example.webshop.dto.IzdanjeDTO;
import com.example.webshop.model.Casopis;
import org.springframework.http.ResponseEntity;

public interface KPService {

    public void createLinks(Long casopisId);

    public String completePayment(String uuid, Long nacinPlacanjaId);

    public Long addNewNumber(IzdanjeDTO dto);

    public String getRedirectUrl(String uapId,String username);

    public String changePayed(String uuid, Boolean success, String username);
}
