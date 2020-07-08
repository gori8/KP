package com.example.webshop.newPaperServices;

import com.example.webshop.dto.PotvrdaRecenzentaDTO;
import com.example.webshop.dto.UredniciRecenzentiDTO;
import com.example.webshop.model.*;
import com.example.webshop.repository.CasopisRepository;
import com.example.webshop.repository.KorisnikElasticRepository;
import com.example.webshop.repository.KorisnikRepository;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class UredniciRecenzentiService implements JavaDelegate {

    @Autowired
    IdentityService identityService;

    @Autowired
    KorisnikRepository korisnikRepository;

    @Autowired
    CasopisRepository casopisRepository;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long casopisId = (Long)execution.getVariable("id");
        UredniciRecenzentiDTO urDTO = (UredniciRecenzentiDTO)execution.getVariable("uredniciRecenzenti");

        Casopis casopis = casopisRepository.getOne(casopisId);

        List<Korisnik> urednici = new ArrayList<>();
        List<Korisnik> recenzenti = new ArrayList<>();

        for (String u : urDTO.getUrednici()) {
            Korisnik urednik = korisnikRepository.findOneByUsername(u);
            if(urednik != null){
                urednici.add(urednik);
                urednik.getCasopisiUrednik().add(casopis);
                korisnikRepository.save(urednik);
            }
        }
        casopis.setUrednici(urednici);

        for (String r : urDTO.getRecenzenti()) {
            Korisnik recenzent = korisnikRepository.findOneByUsername(r);
            if(recenzent != null){
                recenzenti.add(recenzent);
                recenzent.getCasopisiRecenzent().add(casopis);
                korisnikRepository.save(recenzent);
            }
        }
        casopis.setRecenzenti(recenzenti);

        casopis = casopisRepository.save(casopis);
    }
}
