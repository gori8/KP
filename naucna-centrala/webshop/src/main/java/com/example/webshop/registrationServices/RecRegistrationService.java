package com.example.webshop.registrationServices;

import com.example.webshop.model.Authority;
import com.example.webshop.model.Korisnik;
import com.example.webshop.repository.AuthorityRepository;
import com.example.webshop.repository.KorisnikRepository;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class RecRegistrationService implements JavaDelegate {
    @Autowired
    IdentityService identityService;

    @Autowired
    KorisnikRepository korisnikRepository;

    @Autowired
    AuthorityRepository authorityRepository;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("Recenzent ODOBREN!");

        Long userId = (Long)execution.getVariable("id");

        Korisnik korisnik = korisnikRepository.getOne(userId);

        Authority recenzentAuthority = authorityRepository.getOne(3L);
        List<Authority> authorities = new ArrayList<>();
        authorities.add(recenzentAuthority);
        korisnik.setAuthorities(authorities);

        korisnikRepository.save(korisnik);
    }
}
