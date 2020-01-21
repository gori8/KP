package com.example.webshop.repository;

import com.example.webshop.model.Korisnik;
import com.example.webshop.model.NaucnaOblast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KorisnikRepository extends JpaRepository<Korisnik, Long> {
    Korisnik findOneByUsername(String username);

    @Query(value = "SELECT k FROM Korisnik k LEFT OUTER JOIN k.naucneOblasti no WHERE no.id IN ?1")
    List<Korisnik> findUrednikeForNaucnaOblast(List<NaucnaOblast> naucnaOblastList);
}
