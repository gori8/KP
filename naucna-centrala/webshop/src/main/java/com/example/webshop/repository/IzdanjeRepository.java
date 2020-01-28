package com.example.webshop.repository;

import com.example.webshop.model.Izdanje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IzdanjeRepository extends JpaRepository<Izdanje, Long> {

}
