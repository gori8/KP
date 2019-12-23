package com.example.userandpaymentinfo.repository;

import com.example.userandpaymentinfo.model.Casopis;
import com.example.userandpaymentinfo.model.NaucnaOblast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NaucnaOblastRepository extends JpaRepository<NaucnaOblast, Long> {

    NaucnaOblast findOneById(Long id);
}
