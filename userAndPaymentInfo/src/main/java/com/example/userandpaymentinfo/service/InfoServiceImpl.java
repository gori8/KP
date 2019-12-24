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
    public List<NacinPlacanjaDTO> getNacinePlacanjaZaCasopis(Long casopisId){
        Casopis casopis = casopisRepository.findOneById(casopisId);

        List<NacinPlacanjaDTO> ret = new ArrayList<NacinPlacanjaDTO>();

        for (NacinPlacanja np:casopis.getNacinPlacanjaList()) {
            NacinPlacanjaDTO npDTO = new NacinPlacanjaDTO(np.getId(),np.getNacinPlacanja());
            ret.add(npDTO);
        }

        return ret;
    }
}
