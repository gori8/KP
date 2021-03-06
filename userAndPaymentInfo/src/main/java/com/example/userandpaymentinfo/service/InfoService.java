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

    AmountAndUrlDTO getAmountAndUrlSub(String id);

    public void register(RegisterDTO dto);

    ReturnLinksDTO add(CreateLinksDTO dto);

    public String registrationCompleted(RegistrationCompletedDTO dto);
    public Object getForm(Long nacinPlacanjaId, String uuid);
}
