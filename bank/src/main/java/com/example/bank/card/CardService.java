package com.example.bank.card;

import com.example.bank.model.Card;

import java.util.List;

public interface CardService {
    List<Card> findAll();

    Card findByPan(String pan);
}
