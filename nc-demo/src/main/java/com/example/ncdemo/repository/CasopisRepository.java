package com.example.ncdemo.repository;

import com.example.ncdemo.model.Casopis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CasopisRepository extends JpaRepository<Casopis, Long> {
    Casopis findOneByUuid(UUID uuid);
}
