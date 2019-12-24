package com.example.demo.repository;

import com.example.demo.model.Kupovina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KupovinaRepository extends JpaRepository<Kupovina, Long> {

    Kupovina findOneById(Long id);
}
