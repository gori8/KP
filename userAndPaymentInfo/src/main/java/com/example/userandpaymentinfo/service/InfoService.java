package com.example.userandpaymentinfo.service;

import com.example.userandpaymentinfo.dto.CasopisDTO;
import com.example.userandpaymentinfo.dto.PodaciORacunuDTO;
import com.example.userandpaymentinfo.dto.UrednikDTO;
import com.example.userandpaymentinfo.model.Casopis;
import com.example.userandpaymentinfo.model.PodaciORacunu;
import com.example.userandpaymentinfo.model.Urednik;

import java.util.List;

public interface InfoService {

    public Urednik addUrednik(UrednikDTO urednikDTO);
    public Urednik updateUrednik(UrednikDTO urednikDTO);
    public List<Urednik> getAllUrednik();

    public Casopis addCasopis(CasopisDTO casopisDTO);
    public Casopis updateCasopis(CasopisDTO casopisDTO);
    public List<Casopis> getAllCasopisi();

    public PodaciORacunu addPodaciORacunu(PodaciORacunuDTO podaciORacunuDTO, Long id);
    public PodaciORacunu updatePodaciORacunu(PodaciORacunuDTO podaciORacunuDTO, Long id);
    public List<PodaciORacunu> getSveRacuneJednogCasopisa(Long idCasopis);
}
