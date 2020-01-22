package com.example.webshop.repository;

import com.example.webshop.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;



public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Authority findOneByName(String name);
}
