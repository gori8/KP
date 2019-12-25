package com.example.bank.card;

import com.example.bank.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {
    Card findOneByPan(String pan);
}
