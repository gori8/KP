package com.example.webshop.repository;

import com.example.webshop.model.Korisnik;
import com.example.webshop.model.NaucnaOblast;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NaucnaOblastRepository extends JpaRepository<NaucnaOblast, Long> {
}
