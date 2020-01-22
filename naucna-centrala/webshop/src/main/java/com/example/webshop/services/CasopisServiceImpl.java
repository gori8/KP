package com.example.webshop.services;

import com.example.webshop.dto.TaskLinkDTO;
import com.example.webshop.model.Casopis;
import com.example.webshop.model.Korisnik;
import com.example.webshop.model.Link;
import com.example.webshop.repository.KorisnikRepository;
import com.example.webshop.repository.NacinPlacanjaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CasopisServiceImpl implements CasopisService {

    @Autowired
    KorisnikRepository korisnikRepository;

    @Autowired
    NacinPlacanjaRepository nacinPlacanjaRepository;

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
}
