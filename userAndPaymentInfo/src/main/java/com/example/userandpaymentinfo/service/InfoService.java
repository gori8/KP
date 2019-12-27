package com.example.userandpaymentinfo.service;

import com.example.userandpaymentinfo.dto.CasopisDTO;
import com.example.userandpaymentinfo.dto.NacinPlacanjaDTO;
import com.example.userandpaymentinfo.dto.RedirectUrlDTO;
import com.example.userandpaymentinfo.dto.UrlDTO;
import com.example.userandpaymentinfo.model.Casopis;

import java.util.List;

public interface InfoService {

    public Casopis editCasopis(CasopisDTO casopisDTO) throws Exception;
    public Casopis updateCasopis(CasopisDTO casopisDTO);
    public List<Casopis> getAllCasopisi();
    public List<NacinPlacanjaDTO> getNacinePlacanjaZaCasopis(String casopisId);
    public UrlDTO getUrl(RedirectUrlDTO redirectUrlDTO);
}
