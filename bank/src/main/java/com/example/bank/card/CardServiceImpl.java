package com.example.bank.card;

import com.example.bank.card.CardService;
import com.example.bank.model.Card;
import com.example.bank.card.CardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;

    public CardServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Override
    public List<Card> findAll() {
        return cardRepository.findAll();
    }

    @Override
    public Card findByPan(String pan) {
        return cardRepository.findOneByPan(pan);
    }
}
