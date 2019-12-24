package com.example.demo.repository;

import com.example.demo.model.Casopis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CasopisRepository extends JpaRepository<Casopis, Long> {

    Casopis findOneById(Long id);
}
