package rs.ac.uns.ftn.bank.transaction;

import rs.ac.uns.ftn.bank.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}