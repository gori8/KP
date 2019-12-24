package com.example.demo.repository;

import com.example.demo.model.NacinPlacanja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NacinPlacanjaRepository extends JpaRepository<NacinPlacanja, Long> {

    NacinPlacanja findOneById(Long id);
}
