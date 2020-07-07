package com.example.webshop.repository;

import com.example.webshop.model.Rad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RadRepository extends JpaRepository<Rad, Long> {
}
