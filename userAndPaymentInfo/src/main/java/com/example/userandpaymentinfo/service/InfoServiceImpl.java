package com.example.userandpaymentinfo.service;

import com.example.userandpaymentinfo.converters.BrojRacunaConverter;
import com.example.userandpaymentinfo.dto.*;
import com.example.userandpaymentinfo.model.*;
import com.example.userandpaymentinfo.repository.*;
import com.example.userandpaymentinfo.util.Base64Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class InfoServiceImpl implements InfoService{

    @Autowired
    CasopisRepository casopisRepository;

    @Autowired
    PodaciORacunuRepository podaciORacunuRepository;


    @Override
    public Casopis addCasopis(CasopisDTO casopisDTO) {

    Casopis newCasopis = new Casopis();

    newCasopis.setNaziv(casopisDTO.getNaziv());
    newCasopis.setIssn(casopisDTO.getIssn());
    newCasopis.setAutorPlaca(casopisDTO.getAutorPlaca());

    return casopisRepository.save(newCasopis);

    }

    @Override
    public Casopis updateCasopis(CasopisDTO casopisDTO) {

        Casopis updateCasopis = casopisRepository.findOneById(casopisDTO.getId());

        updateCasopis.setNaziv(casopisDTO.getNaziv());
        updateCasopis.setIssn(casopisDTO.getIssn());
        updateCasopis.setAutorPlaca(casopisDTO.getAutorPlaca());

        return casopisRepository.save(updateCasopis);

    }

    @Override
    public List<Casopis> getAllCasopisi() {
        return casopisRepository.findAll();
    }

    @Override
    public PodaciORacunu addPodaciORacunu(PodaciORacunuDTO podaciORacunuDTO, Long id) {

        PodaciORacunu newRacun = new PodaciORacunu();

        newRacun.setNacinPlacanja(podaciORacunuDTO.getNacinPlacanja());
        newRacun.setBrojRacuna(podaciORacunuDTO.getBrojRacuna());
        newRacun.setStanjeNaRacunu(podaciORacunuDTO.getStanjeNaRacunu());
        newRacun.setCasopis(casopisRepository.findOneById(id));

        return podaciORacunuRepository.save(newRacun);
    }

    @Override
    public PodaciORacunu updatePodaciORacunu(PodaciORacunuDTO podaciORacunuDTO, Long id) {

        PodaciORacunu updateRacun = podaciORacunuRepository.findOneById(id);

        updateRacun.setNacinPlacanja(podaciORacunuDTO.getNacinPlacanja());
        updateRacun.setBrojRacuna(podaciORacunuDTO.getBrojRacuna());
        updateRacun.setStanjeNaRacunu(podaciORacunuDTO.getStanjeNaRacunu());

        return podaciORacunuRepository.save(updateRacun);
    }

    @Override
    public List<PodaciORacunu> getSveRacuneJednogCasopisa(Long idCasopisa) {
        return podaciORacunuRepository.findAllByCasopis_Id(idCasopisa);

    }
}
