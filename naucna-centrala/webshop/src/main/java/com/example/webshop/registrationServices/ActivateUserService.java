package com.example.webshop.registrationServices;

import com.example.webshop.model.Korisnik;
import com.example.webshop.repository.KorisnikRepository;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivateUserService implements JavaDelegate {
    @Autowired
    IdentityService identityService;

    @Autowired
    KorisnikRepository korisnikRepository;

    @Override
    public void execute(DelegateExecution execution) {

        Long userId = (Long)execution.getVariable("id");

        Korisnik korisnik = korisnikRepository.getOne(userId);
        korisnik.setAktiviran(true);
        korisnikRepository.save(korisnik);
    }
}
