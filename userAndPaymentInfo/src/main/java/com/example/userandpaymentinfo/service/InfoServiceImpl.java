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
import java.util.UUID;


@Service
public class InfoServiceImpl implements InfoService{

    private static final String frontUrl = "http://localhost:4200";

    @Autowired
    CasopisRepository casopisRepository;

    @Autowired
    NacinPlacanjaRepository nacinPlacanjaRepository;

    @Override
    public Casopis editCasopis(CasopisDTO casopisDTO) throws Exception {

    Casopis newCasopis = null;
    newCasopis = casopisRepository.findOneByIssn(casopisDTO.getIssn());

    NacinPlacanja np = null;

    if(newCasopis == null){
        newCasopis = new Casopis();
        newCasopis.setNaziv(casopisDTO.getNaziv());
        newCasopis.setIssn(casopisDTO.getIssn());
        newCasopis.setUuid(UUID.randomUUID());
    }
    else{
        np = nacinPlacanjaRepository.findByIdIfInCasopis(casopisDTO.getNacinPlacanjaId(),newCasopis.getId());
    }

    if(np == null){
        np = nacinPlacanjaRepository.getOne(casopisDTO.getNacinPlacanjaId());
        newCasopis.getNacinPlacanjaList().add(np);
        np.getCasopisList().add(newCasopis);

        nacinPlacanjaRepository.save(np);
    }else{
        throw new Exception();
    }

    return casopisRepository.save(newCasopis);

    }

    @Override
    public Casopis updateCasopis(CasopisDTO casopisDTO) {

        Casopis updateCasopis = casopisRepository.findOneById(casopisDTO.getId());

        updateCasopis.setNaziv(casopisDTO.getNaziv());
        updateCasopis.setIssn(casopisDTO.getIssn());

        return casopisRepository.save(updateCasopis);

    }

    @Override
    public List<Casopis> getAllCasopisi() {
        return casopisRepository.findAll();
    }

    @Override
    public List<NacinPlacanjaDTO> getNacinePlacanjaZaCasopis(String casopisId){
        UUID uuid = UUID.fromString(casopisId);
        Casopis casopis = casopisRepository.findOneByUuid(uuid);

        List<NacinPlacanjaDTO> ret = new ArrayList<NacinPlacanjaDTO>();

        for (NacinPlacanja np:casopis.getNacinPlacanjaList()) {
            NacinPlacanjaDTO npDTO = new NacinPlacanjaDTO(np.getId(),np.getNacinPlacanja(),np.getUrl());
            ret.add(npDTO);
        }

        return ret;
    }

    @Override
    public UrlDTO getUrl(RedirectUrlDTO redirectUrlDTO){
        UUID uuid = UUID.fromString(redirectUrlDTO.getId());
        System.out.println(redirectUrlDTO.getId());
        Casopis c = casopisRepository.findOneByUuid(uuid);

        c.setRedirectUrl(redirectUrlDTO.getRedirectUrl());

        casopisRepository.save(c);

        UrlDTO url = new UrlDTO(frontUrl+"/"+uuid.toString());

        return url;
    }

    @Override
    public AmountAndUrlDTO getAmountAndUrl(String id) {
        UUID uuid = UUID.fromString(id);
        Casopis casopis=casopisRepository.findOneByUuid(uuid);
        AmountAndUrlDTO dto=new AmountAndUrlDTO();
        dto.setAmount(casopis.getAmount());
        dto.setRedirectUrl(casopis.getRedirectUrl());
        return dto;
    }
}
