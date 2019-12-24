package com.example.demo.service;

import com.example.demo.dto.KupovinaDTO;
import com.example.demo.model.Kupovina;
import com.example.demo.model.PodaciORacunuKupca;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DemoServiceImpl implements DemoService{

    @Autowired
    CasopisRepository casopisRepository;

    @Autowired
    PodaciORacunuKupcaRepository podaciORacunuKupcaRepository;

    @Autowired
    PodaciORacunuRepository podaciORacunuRepository;

    @Autowired
    NacinPlacanjaRepository nacinPlacanjaRepository;

    @Autowired
    KupovinaRepository kupovinaRepository;

    @Override
    public Kupovina kupiCasopis(KupovinaDTO kupovinaDTO) {

        Kupovina kupovina = new Kupovina();

        kupovina.setCasopisId(kupovinaDTO.getCasopisId());
        kupovina.setKupacId(kupovinaDTO.getKupacId());
        kupovina.setIznos(kupovinaDTO.getIznos());
        kupovina.setRacunCasopisaId(kupovinaDTO.getRacunCasopisaId());

        return kupovinaRepository.save(kupovina);
    }
}
