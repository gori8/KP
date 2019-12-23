package com.example.userandpaymentinfo.repository;

import com.example.userandpaymentinfo.model.PodaciORacunu;
import com.example.userandpaymentinfo.model.Recenzent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecenzentRepository extends JpaRepository<Recenzent, Long> {

    Recenzent findOneById(Long id);

}
