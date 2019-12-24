package com.example.userandpaymentinfo.service;

import com.example.userandpaymentinfo.dto.CasopisDTO;
import com.example.userandpaymentinfo.dto.NacinPlacanjaDTO;
import com.example.userandpaymentinfo.model.Casopis;

import java.util.List;

public interface InfoService {

    public Casopis addCasopis(CasopisDTO casopisDTO);
    public Casopis updateCasopis(CasopisDTO casopisDTO);
    public List<Casopis> getAllCasopisi();
    public List<NacinPlacanjaDTO> getNacinePlacanjaZaCasopis(Long casopisId);
}
