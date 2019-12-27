package com.example.userandpaymentinfo.repository;

import com.example.userandpaymentinfo.model.Casopis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CasopisRepository extends JpaRepository<Casopis, Long> {

    Casopis findOneById(Long id);
    Casopis findOneByIssn(String issn);
    Casopis findOneByUuid(String uuid);
}
