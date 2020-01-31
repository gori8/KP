package com.example.webshop.services;

import com.example.webshop.dto.CasopisDTO;
import com.example.webshop.dto.IzdanjeDTO;
import com.example.webshop.dto.TaskLinkDTO;
import com.example.webshop.model.Casopis;
import com.example.webshop.model.Izdanje;
import com.example.webshop.model.Korisnik;
import com.example.webshop.model.Link;
import com.example.webshop.repository.CasopisRepository;
import com.example.webshop.repository.KorisnikRepository;
import com.example.webshop.repository.NacinPlacanjaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Service
public class CasopisServiceImpl implements CasopisService {

    @Autowired
    KorisnikRepository korisnikRepository;

    @Autowired
    NacinPlacanjaRepository nacinPlacanjaRepository;

    @Autowired
    CasopisRepository casopisRepository;

    @Override
    public List<TaskLinkDTO> getTasks(String username){
        Korisnik korisnik = korisnikRepository.findOneByUsername(username);

        List<TaskLinkDTO> ret = new ArrayList<>();

        for (Casopis casopis:korisnik.getCasopisiGlavni()) {
            for (Link link:casopis.getLinkovi()) {
                if(link.getCompleted() == false){
                    TaskLinkDTO task = new TaskLinkDTO();

                    task.setLink(link.getUrl());
                    task.setCasopis(casopis.getNaziv());
                    task.setNacinPlacanja(nacinPlacanjaRepository.getOne(link.getNacinPlacanja()).getNaziv());
                    ret.add(task);
                }
            }
        }
        return ret;
    }

    @Override
    public CasopisDTO getPaper(Long id){
        Casopis casopis = casopisRepository.getOne(id);
        CasopisDTO dto = new CasopisDTO();
        dto.setNaziv(casopis.getNaziv());
        dto.setAktiviran(casopis.getAktiviran());
        dto.setClanarina(casopis.getClanarina());
        dto.setId(casopis.getId());
        dto.setIssn(casopis.getIssn());
        dto.setKomeSeNaplacuje(casopis.getKomeSeNaplacuje());

        return dto;
    }

    @Override
    public CasopisDTO getNumbersForPaper(Long id){
        Casopis casopis = casopisRepository.getOne(id);
        CasopisDTO dto = new CasopisDTO();
        dto.setNaziv(casopis.getNaziv());
        dto.setAktiviran(casopis.getAktiviran());
        dto.setClanarina(casopis.getClanarina());
        dto.setId(casopis.getId());
        dto.setIssn(casopis.getIssn());
        dto.setKomeSeNaplacuje(casopis.getKomeSeNaplacuje());
        dto.setIzdanja(new ArrayList<>());
        dto.setUrednik(casopis.getGlavniUrednik().getUsername());
        dto.setUuid(casopis.getUuid().toString());
        for (Izdanje izdanje:casopis.getIzdanja()) {
            IzdanjeDTO izdanjeDTO = new IzdanjeDTO();
            izdanjeDTO.setBroj(izdanje.getBroj());
            izdanjeDTO.setCasopisId(dto.getId());
            izdanjeDTO.setCena(izdanje.getCena());
            izdanjeDTO.setId(izdanje.getId());
            izdanjeDTO.setNaziv(izdanje.getNaziv());
            izdanjeDTO.setDatumIzdanja(izdanje.getDatumIzdanja());
            izdanjeDTO.setUuid(izdanje.getUuid().toString());

            dto.getIzdanja().add(izdanjeDTO);
        }

        return dto;

    }

    public List<CasopisDTO> getBoughtItems(String username){
        Korisnik korisnik = korisnikRepository.findOneByUsername(username);

        HashMap<Long,CasopisDTO> casopisiMap = new HashMap<>();

        for (Izdanje izdanje:korisnik.getCasopisiKupci()) {
            IzdanjeDTO izdanjeDTO = new IzdanjeDTO();
            izdanjeDTO.setBroj(izdanje.getBroj());
            izdanjeDTO.setCasopisId(izdanje.getCasopis().getId());
            izdanjeDTO.setCena(izdanje.getCena());
            izdanjeDTO.setId(izdanje.getId());
            izdanjeDTO.setNaziv(izdanje.getNaziv());
            izdanjeDTO.setDatumIzdanja(izdanje.getDatumIzdanja());
            izdanjeDTO.setUuid(izdanje.getUuid().toString());

            Casopis casopis = izdanje.getCasopis();
            CasopisDTO casopisDTO;
            if(casopisiMap.containsKey(casopis.getId())){
                casopisDTO = casopisiMap.get(casopis.getId());
            }else{
                casopisDTO = new CasopisDTO();
                casopisDTO.setNaziv(casopis.getNaziv());
                casopisDTO.setAktiviran(casopis.getAktiviran());
                casopisDTO.setClanarina(casopis.getClanarina());
                casopisDTO.setId(casopis.getId());
                casopisDTO.setIssn(casopis.getIssn());
                casopisDTO.setKomeSeNaplacuje(casopis.getKomeSeNaplacuje());
                casopisDTO.setIzdanja(new ArrayList<>());
                casopisDTO.setUrednik(casopis.getGlavniUrednik().getUsername());
                casopisDTO.setUuid(casopis.getUuid().toString());

                casopisiMap.put(casopisDTO.getId(),casopisDTO);
            }

            casopisDTO.getIzdanja().add(izdanjeDTO);
        }

        return (List<CasopisDTO>) casopisiMap.values();
    }
}
