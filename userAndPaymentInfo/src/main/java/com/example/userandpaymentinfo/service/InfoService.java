package com.example.userandpaymentinfo.service;

import com.example.userandpaymentinfo.dto.*;
import com.example.userandpaymentinfo.model.Item;

import java.util.List;

public interface InfoService {

    public Item editCasopis(CasopisDTO casopisDTO) throws Exception;
    public Item updateCasopis(CasopisDTO casopisDTO);
    public List<Item> getAllCasopisi();
    public List<NacinPlacanjaDTO> getNacinePlacanjaZaCasopis(String casopisId);
    public UrlDTO getUrl(RedirectUrlDTO redirectUrlDTO);
    public AmountAndUrlDTO getAmountAndUrl(String id);
    public ReturnLinksDTO createLinks(CreateLinksDTO dto);
    public String registrationCompleted(RegistrationCompletedDTO dto);
}
