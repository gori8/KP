package com.example.webshop.newPaperServices;

import com.example.webshop.model.Casopis;
import com.example.webshop.model.Korisnik;
import com.example.webshop.repository.CasopisRepository;
import com.example.webshop.repository.KorisnikRepository;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ActivatePaperService implements JavaDelegate {
    @Autowired
    IdentityService identityService;

    @Autowired
    CasopisRepository casopisRepository;

    @Override
    public void execute(DelegateExecution execution) {

        Long casopisId = (Long)execution.getVariable("id");

        Casopis casopis = casopisRepository.getOne(casopisId);
        casopis.setAktiviran(true);
        casopisRepository.save(casopis);
    }
}
