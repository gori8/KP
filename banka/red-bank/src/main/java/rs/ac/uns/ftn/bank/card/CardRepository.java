package rs.ac.uns.ftn.bank.card;

import rs.ac.uns.ftn.bank.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {
    Card findOneByPan(String pan);
}
