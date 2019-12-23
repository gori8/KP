package com.example.userandpaymentinfo.repository;

import com.example.userandpaymentinfo.model.Urednik;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrednikRepository extends JpaRepository<Urednik, Long> {

    Urednik findOneById(Long id);
}
