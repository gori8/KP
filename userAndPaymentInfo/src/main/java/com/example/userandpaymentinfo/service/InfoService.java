package com.example.userandpaymentinfo.service;

import com.example.userandpaymentinfo.dto.*;
import com.example.userandpaymentinfo.model.Item;
import rs.ac.uns.ftn.url.AmountAndUrlDTO;

import java.util.List;
import java.util.Map;

public interface InfoService {

    public Item editCasopis(CasopisDTO casopisDTO) throws Exception;
    public Item updateCasopis(CasopisDTO casopisDTO);
    public List<Item> getAllCasopisi();
    public List<NacinPlacanjaDTO> getNacinePlacanjaZaCasopis(String casopisId);
    public UrlDTO getUrl(RedirectUrlDTO redirectUrlDTO);
    public AmountAndUrlDTO getAmountAndUrl(String id);
    public ReturnLinksDTO register(CreateLinksDTO dto);
    public String registrationCompleted(RegistrationCompletedDTO dto);
    public Object getForm(String folder, String uuid);
    public Map<String,String> getImage(String folder, String name);
}
