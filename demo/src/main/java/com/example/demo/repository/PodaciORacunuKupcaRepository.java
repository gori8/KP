package com.example.demo.repository;


import com.example.demo.model.PodaciORacunuKupca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PodaciORacunuKupcaRepository extends JpaRepository<PodaciORacunuKupca, Long> {

    PodaciORacunuKupca findOneById(Long id);
    List<PodaciORacunuKupca> findAllByKupac_Id(Long idKupac);
}
