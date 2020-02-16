package com.example.webshop.repository;

import com.example.webshop.model.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PlanRepository extends JpaRepository<Plan, Long> {

    Plan findOneByUuid(UUID uuid);

}