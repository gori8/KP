package com.example.webshop.repository;

import com.example.webshop.model.Casopis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CasopisRepository extends JpaRepository<Casopis, Long> {

    @Query(value = "SELECT MAX(c.issn) FROM Casopis c")
    Long findMaxIssn();

    @Query(value = "SELECT c FROM Casopis c WHERE c.aktiviran=1")
    List<Casopis> findAllActive();
}
