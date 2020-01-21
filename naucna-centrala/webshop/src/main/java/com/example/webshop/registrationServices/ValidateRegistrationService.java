package com.example.webshop.registrationServices;

import com.example.webshop.dto.FormSubmissionDto;
import com.example.webshop.model.Authority;
import com.example.webshop.model.Korisnik;
import com.example.webshop.model.NaucnaOblast;
import com.example.webshop.repository.AuthorityRepository;
import com.example.webshop.repository.KorisnikRepository;
import com.example.webshop.repository.NaucnaOblastRepository;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ValidateRegistrationService implements JavaDelegate {
    @Autowired
    IdentityService identityService;

    @Autowired
    KorisnikRepository korisnikRepository;

    @Autowired
    NaucnaOblastRepository naucnaOblastRepository;

    @Autowired
    AuthorityRepository authorityRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        boolean validation = true;

        List<FormSubmissionDto> registracionaForma = (List<FormSubmissionDto>)execution.getVariable("registracionaForma");
        System.out.println(registracionaForma);

        Korisnik korisnik = new Korisnik();
        korisnik.setAktiviran(false);

        User user = identityService.newUser("");

        for (FormSubmissionDto formField : registracionaForma) {
            if(formField.getFieldId().equals("ime")) {
                if(formField.getFieldValue()==null || formField.getFieldValue().trim()==""){
                    validation = false;
                    break;
                }
                else {
                    korisnik.setIme(formField.getFieldValue());
                }
            }
            if(formField.getFieldId().equals("prezime")) {
                if(formField.getFieldValue()==null || formField.getFieldValue().trim()==""){
                    validation = false;
                    break;
                }
                else {
                    korisnik.setPrezime(formField.getFieldValue());
                }
            }
            if(formField.getFieldId().equals("grad")) {
                if(formField.getFieldValue()==null || formField.getFieldValue().trim()==""){
                    validation = false;
                    break;
                }
                else {
                    korisnik.setGrad(formField.getFieldValue());
                }
            }
            if(formField.getFieldId().equals("drzava")) {
                if(formField.getFieldValue()==null || formField.getFieldValue().trim()==""){
                    validation = false;
                    break;
                }
                else {
                    korisnik.setDrzava(formField.getFieldValue());
                }
            }
            if(formField.getFieldId().equals("titula")) {
                if(formField.getFieldValue()==null || formField.getFieldValue().trim()==""){
                    validation = false;
                    break;
                }
                else {
                    korisnik.setTitula(formField.getFieldValue());
                }
            }
            if(formField.getFieldId().equals("email")) {
                Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(formField.getFieldValue());

                if(formField.getFieldValue()==null || formField.getFieldValue().trim()=="" || matcher.find()==false){
                    validation = false;
                    break;
                }
                else {
                    korisnik.setEmail(formField.getFieldValue());
                    execution.setVariable("email", formField.getFieldValue());
                }
            }
            if(formField.getFieldId().equals("username")) {
                Korisnik postojeciKorisnik = korisnikRepository.findOneByUsername(formField.getFieldValue());
                if(formField.getFieldValue()==null || formField.getFieldValue().trim()=="" || postojeciKorisnik!=null){
                    validation = false;
                    break;
                }
                else {
                    korisnik.setUsername(formField.getFieldValue());
                    user.setId(formField.getFieldValue());
                }
            }
            if(formField.getFieldId().equals("password")) {
                if(formField.getFieldValue()==null || formField.getFieldValue().trim()==""){
                    validation = false;
                    break;
                }
                else {
                    korisnik.setPassword(passwordEncoder().encode(formField.getFieldValue()));
                    user.setPassword(formField.getFieldValue());
                }
            }
            if(formField.getFieldId().equals("recenzent")) {
                if(formField.getFieldValue()==null){
                    validation = false;
                    break;
                }
                else {
                    korisnik.setRecenzent(Boolean.valueOf(formField.getFieldValue()));
                }
            }
            if(formField.getFieldId().contains("naucnaOblast")){
                if(formField.getFieldValue()==null || formField.getFieldValue().trim()==""){
                    validation = false;
                    break;
                }
                else {
                    NaucnaOblast naucnaOblast = naucnaOblastRepository.getOne(Long.parseLong(formField.getFieldValue(), 10));
                    korisnik.getNaucneOblasti().add(naucnaOblast);
                    naucnaOblast.getKorisnici().add(korisnik);
                    naucnaOblastRepository.save(naucnaOblast);
                }
            }
        }

        if(korisnik.getNaucneOblasti().size()==0){
            validation=false;
        }

        if(validation==false){
            execution.setVariable("validacija", false);
            return;
        }

        Authority authority = authorityRepository.findOneByName("ROLE_USER");
        List<Authority> authorities = new ArrayList<>();
        authorities.add(authority);
        korisnik.setAuthorities(authorities);

        korisnik = korisnikRepository.save(korisnik);
        identityService.saveUser(user);

        execution.setVariable("validacija", true);
        execution.setVariable("id", korisnik.getId());

        System.out.println((Long)execution.getVariable("id"));
    }
}
