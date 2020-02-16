package com.example.webshop.services;

import com.example.webshop.dto.CallPayPalSubscriptionDTO;
import com.example.webshop.dto.IzdanjeDTO;
import com.example.webshop.dto.PlanDTO;
import com.example.webshop.model.Casopis;
import com.example.webshop.model.Plan;
import org.springframework.http.ResponseEntity;

import java.util.Date;

public interface KPService {

    public void createLinks(Plan plan) throws Exception;

    public String completePayment(String uuid, Long nacinPlacanjaId);

    public Long addNewNumber(IzdanjeDTO dto);

    public String getRedirectUrl(String uapId,String username);

    public String changePayed(String uuid, Boolean success, String username);

    public String callPayPalSubscription(CallPayPalSubscriptionDTO dto);

    public String donePayPalSubsctiption(String uuid, Boolean success, String username, Date datumIsticanja);
}
