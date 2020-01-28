package rs.ac.uns.ftn.bank.card;

import rs.ac.uns.ftn.bank.model.Card;

import java.util.List;

public interface CardService {
    List<Card> findAll();

    Card findByPan(String pan);
}
