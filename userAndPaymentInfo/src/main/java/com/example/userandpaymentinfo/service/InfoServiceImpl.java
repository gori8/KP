package com.example.userandpaymentinfo.service;

import com.example.userandpaymentinfo.dto.*;
import com.example.userandpaymentinfo.model.*;
import com.example.userandpaymentinfo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class InfoServiceImpl implements InfoService{

    @Autowired
    CasopisRepository casopisRepository;

    @Autowired
    NaucnaOblastRepository naucnaOblastRepository;

    @Autowired
    UrednikRepository urednikRepository;

    @Autowired
    PodaciORacunuRepository podaciORacunuRepository;

    @Autowired
    RecenzentRepository recenzentRepository;

    @Override
    public Urednik addUrednik(UrednikDTO urednikDTO) {

        Urednik newUrednik = new Urednik();
        newUrednik.setUsername(urednikDTO.getUsername());
        newUrednik.setPassword(urednikDTO.getPassword());
        newUrednik.setIme(urednikDTO.getIme());
        newUrednik.setPrezime(urednikDTO.getPrezime());
        newUrednik.setTitula(urednikDTO.getTitula());
        newUrednik.setGrad(urednikDTO.getGrad());
        newUrednik.setDrzava(urednikDTO.getDrzava());
        newUrednik.setEmail(urednikDTO.getEmail());
        newUrednik.setOdobren(false);
        newUrednik.setCasopis(casopisRepository.findOneById(urednikDTO.getCasopisId()));

        List<NaucnaOblast> lista = new ArrayList<>();
        for(NaucnaOblastDTO no : urednikDTO.getListaNaucnihOblasti()) {
           lista.add(naucnaOblastRepository.findOneById(no.getId()));
        }
        newUrednik.setNaucneOblasti(lista);

        return urednikRepository.save(newUrednik);
    }

    @Override
    public Urednik updateUrednik(UrednikDTO urednikDTO) {
        Urednik updateUrednik = urednikRepository.findOneById(urednikDTO.getId());

        updateUrednik.setUsername(urednikDTO.getUsername());
        updateUrednik.setPassword(urednikDTO.getPassword());
        updateUrednik.setIme(urednikDTO.getIme());
        updateUrednik.setPrezime(urednikDTO.getPrezime());
        updateUrednik.setTitula(urednikDTO.getTitula());
        updateUrednik.setGrad(urednikDTO.getGrad());
        updateUrednik.setDrzava(urednikDTO.getDrzava());
        updateUrednik.setEmail(urednikDTO.getEmail());
        updateUrednik.setOdobren(urednikDTO.getOdobren());
        updateUrednik.setCasopis(casopisRepository.findOneById(urednikDTO.getCasopisId()));

        List<NaucnaOblast> lista = new ArrayList<>();
        for(NaucnaOblastDTO no : urednikDTO.getListaNaucnihOblasti()) {
            lista.add(naucnaOblastRepository.findOneById(no.getId()));
        }
        updateUrednik.setNaucneOblasti(lista);

        return urednikRepository.save(updateUrednik);
    }

    @Override
    public List<Urednik> getAllUrednik() {
        return urednikRepository.findAll();
    }

    @Override
    public Casopis addCasopis(CasopisDTO casopisDTO) {

    Casopis newCasopis = new Casopis();

    newCasopis.setNaziv(casopisDTO.getNaziv());
    newCasopis.setIssn(casopisDTO.getIssn());
    newCasopis.setAutorPlaca(casopisDTO.getAutorPlaca());
    newCasopis.setGlavniUrednik(casopisDTO.getGlavniUrednik());

    List<NaucnaOblast> listaOblasti = new ArrayList<>();
    for(NaucnaOblastDTO no : casopisDTO.getListaNaucnihOblasti()) {
        listaOblasti.add(naucnaOblastRepository.findOneById(no.getId()));
    }
    newCasopis.setNaucneOblasti(listaOblasti);

    return casopisRepository.save(newCasopis);

    }

    @Override
    public Casopis updateCasopis(CasopisDTO casopisDTO) {

        Casopis updateCasopis = casopisRepository.findOneById(casopisDTO.getId());

        updateCasopis.setNaziv(casopisDTO.getNaziv());
        updateCasopis.setIssn(casopisDTO.getIssn());
        updateCasopis.setAutorPlaca(casopisDTO.getAutorPlaca());
        updateCasopis.setGlavniUrednik(casopisDTO.getGlavniUrednik());

        List<NaucnaOblast> listaOblasti = new ArrayList<>();
        for(NaucnaOblastDTO no : casopisDTO.getListaNaucnihOblasti()) {
            listaOblasti.add(naucnaOblastRepository.findOneById(no.getId()));
        }
        updateCasopis.setNaucneOblasti(listaOblasti);

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
        newRacun.setCenaZaPretplatu(podaciORacunuDTO.getCenaZaPretplatu());
        newRacun.setCasopis(casopisRepository.findOneById(id));

        return podaciORacunuRepository.save(newRacun);
    }

    @Override
    public PodaciORacunu updatePodaciORacunu(PodaciORacunuDTO podaciORacunuDTO, Long id) {

        PodaciORacunu updateRacun = podaciORacunuRepository.findOneById(id);

        updateRacun.setNacinPlacanja(podaciORacunuDTO.getNacinPlacanja());
        updateRacun.setBrojRacuna(podaciORacunuDTO.getBrojRacuna());
        updateRacun.setCenaZaPretplatu(podaciORacunuDTO.getCenaZaPretplatu());

        return podaciORacunuRepository.save(updateRacun);
    }

    @Override
    public List<PodaciORacunu> getSveRacuneJednogCasopisa(Long idCasopisa) {
        return podaciORacunuRepository.findAllByCasopis_Id(idCasopisa);
    }
}
