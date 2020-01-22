package com.example.webshop.services;

import com.example.webshop.dto.UserDTO;
import com.example.webshop.model.Korisnik;
import com.example.webshop.security.auth.JwtAuthenticationRequest;

public interface LoginService {

    public Korisnik checkCredentials(JwtAuthenticationRequest request);
}
