package com.example.webshop.services;

import com.example.webshop.model.Korisnik;
import com.example.webshop.repository.AuthorityRepository;
import com.example.webshop.repository.KorisnikRepository;
import com.example.webshop.security.auth.JwtAuthenticationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements  LoginService{


    @Autowired
    KorisnikRepository userRepository;

    @Autowired
    AuthorityRepository authorityRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public Korisnik checkCredentials(JwtAuthenticationRequest request) {
        Korisnik user=userRepository.findOneByUsername(request.getUsername());
        if(user!=null && user.getAktiviran()==true){
            if(passwordEncoder().matches(request.getPassword(),user.getPassword())){
                return user;
            }
        }
        return null;
    }
}