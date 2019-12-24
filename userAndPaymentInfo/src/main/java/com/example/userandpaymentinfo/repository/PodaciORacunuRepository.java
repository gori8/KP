package com.example.userandpaymentinfo.repository;

import com.example.userandpaymentinfo.model.PodaciORacunu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PodaciORacunuRepository extends JpaRepository<PodaciORacunu, Long> {

    PodaciORacunu findOneById(Long id);
    List<PodaciORacunu> findAllByCasopis_Id(Long idCasopis);
}
