package com.example.ncdemo.repository;

import com.example.ncdemo.model.Casopis;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CasopisRepository extends JpaRepository<Casopis, Long> {
}
