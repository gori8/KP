package rs.ac.uns.ftn.bank.card;

import rs.ac.uns.ftn.bank.model.Card;
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
