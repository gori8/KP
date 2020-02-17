package com.example.webshop.services;

import com.example.webshop.dto.CasopisDTO;
import com.example.webshop.dto.IzdanjeDTO;
import com.example.webshop.dto.PlanDTO;
import com.example.webshop.dto.TaskLinkDTO;
import com.example.webshop.model.*;
import com.example.webshop.repository.CasopisRepository;
import com.example.webshop.repository.KorisnikRepository;
import com.example.webshop.repository.NacinPlacanjaRepository;
import com.example.webshop.repository.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CasopisServiceImpl implements CasopisService {

    @Autowired
    KorisnikRepository korisnikRepository;

    @Autowired
    NacinPlacanjaRepository nacinPlacanjaRepository;

    @Autowired
    CasopisRepository casopisRepository;

    @Autowired
    PlanRepository planRepository;

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
        dto.setId(casopis.getId());
        dto.setIssn(casopis.getIssn());
        dto.setKomeSeNaplacuje(casopis.getKomeSeNaplacuje());
        dto.setIzdanja(new ArrayList<>());
        dto.setUrednik(casopis.getGlavniUrednik().getUsername());
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

    @Override
    public List<CasopisDTO> getBoughtItems(String username){
        Korisnik korisnik = korisnikRepository.findOneByUsername(username);

        HashMap<Long,CasopisDTO> casopisiMap = new HashMap<>();
        Date today = new Date();

        for (Pretplata pretplata: korisnik.getPretplate()) {
            Casopis casopis = pretplata.getPlan().getCasopis();

            if(pretplata.getDatumIsticanja().before(today)){
                for (Izdanje izdanje:korisnik.getCasopisiKupci()) {
                    if(izdanje.getCasopis().getId()==casopis.getId()){
                        IzdanjeDTO izdanjeDTO = createIzdanjeDTO(izdanje);

                        CasopisDTO casopisDTO;
                        if(casopisiMap.containsKey(casopis.getId())){
                            casopisDTO = casopisiMap.get(casopis.getId());
                        }else{

                            casopisDTO = createCasopisDTO(casopis);
                            casopisiMap.put(casopisDTO.getId(),casopisDTO);
                        }

                        casopisDTO.getIzdanja().add(izdanjeDTO);
                    }
                }
            }
            else{
                for (Izdanje izdanje:casopis.getIzdanja()) {
                    IzdanjeDTO izdanjeDTO = createIzdanjeDTO(izdanje);

                    CasopisDTO casopisDTO;
                    if(casopisiMap.containsKey(casopis.getId())){
                        casopisDTO = casopisiMap.get(casopis.getId());
                    }else{
                        casopisDTO = createCasopisDTO(casopis);
                        casopisiMap.put(casopisDTO.getId(),casopisDTO);
                    }

                    casopisDTO.getIzdanja().add(izdanjeDTO);
                }
            }
        }

        return new ArrayList<>(casopisiMap.values());
    }

    public CasopisDTO createCasopisDTO(Casopis casopis){
        CasopisDTO casopisDTO = new CasopisDTO();
        casopisDTO.setNaziv(casopis.getNaziv());
        casopisDTO.setAktiviran(casopis.getAktiviran());
        casopisDTO.setId(casopis.getId());
        casopisDTO.setIssn(casopis.getIssn());
        casopisDTO.setKomeSeNaplacuje(casopis.getKomeSeNaplacuje());
        casopisDTO.setIzdanja(new ArrayList<>());
        casopisDTO.setUrednik(casopis.getGlavniUrednik().getUsername());

        return casopisDTO;
    }

    public IzdanjeDTO createIzdanjeDTO(Izdanje izdanje){
        IzdanjeDTO izdanjeDTO = new IzdanjeDTO();
        izdanjeDTO.setBroj(izdanje.getBroj());
        izdanjeDTO.setCasopisId(izdanje.getCasopis().getId());
        izdanjeDTO.setCena(izdanje.getCena());
        izdanjeDTO.setId(izdanje.getId());
        izdanjeDTO.setNaziv(izdanje.getNaziv());
        izdanjeDTO.setDatumIzdanja(izdanje.getDatumIzdanja());
        izdanjeDTO.setUuid(izdanje.getUuid().toString());

        return izdanjeDTO;
    }

    @Override
    public List<IzdanjeDTO> getBoughtItemsForCasopis(String username, Long casopisId){
        Korisnik korisnik = korisnikRepository.findOneByUsername(username);
        List<IzdanjeDTO> ret = new ArrayList<>();

        for (Izdanje izdanje:korisnik.getCasopisiKupci()) {
            if(izdanje.getCasopis().getId()==casopisId){
                IzdanjeDTO izdanjeDTO = new IzdanjeDTO();
                izdanjeDTO.setBroj(izdanje.getBroj());
                izdanjeDTO.setCasopisId(izdanje.getCasopis().getId());
                izdanjeDTO.setCena(izdanje.getCena());
                izdanjeDTO.setId(izdanje.getId());
                izdanjeDTO.setNaziv(izdanje.getNaziv());
                izdanjeDTO.setDatumIzdanja(izdanje.getDatumIzdanja());
                izdanjeDTO.setUuid(izdanje.getUuid().toString());

                ret.add(izdanjeDTO);
            }
        }

        return ret;
    }

    @Override
    public List<Plan> setPlans(List<PlanDTO> dto, Long casopisId){
        Casopis casopis = casopisRepository.getOne(casopisId);

        List<Plan> planovi = new ArrayList<>();
        for (PlanDTO p:dto) {
            Plan plan = new Plan();
            plan.setPeriod(p.getPeriod());
            plan.setUcestalostPerioda(p.getUcestalostPerioda());
            plan.setCena(p.getCena());
            plan.setCasopis(casopis);
            plan = planRepository.save(plan);

            planovi.add(plan);
        }

        casopis.setPlanovi(planovi);
        casopisRepository.save(casopis);

        return planovi;
    }

    @Override
    public List<PlanDTO> getPlansForPaper(Long casopisId){
        List<Plan> planList = planRepository.findAllByCasopisId(casopisId);
        List<PlanDTO> ret = new ArrayList<>();

        for (Plan plan:planList) {
            PlanDTO dto = new PlanDTO();
            dto.setCena(plan.getCena());
            dto.setPeriod(plan.getPeriod());
            dto.setUcestalostPerioda(plan.getUcestalostPerioda());
            dto.setUuid(plan.getUuid().toString());

            ret.add(dto);
        }

        return ret;
    }
}
