package com.example.userandpaymentinfo.repository;

import com.example.userandpaymentinfo.model.NacinPlacanja;
import com.example.userandpaymentinfo.model.NaucnaOblast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NacinPlacanjaRepository extends JpaRepository<NacinPlacanja, Long> {

    NacinPlacanja findOneById(Long id);
}
