package rs.ac.uns.ftn.bank.transaction;

import rs.ac.uns.ftn.bank.model.Transaction;

public interface TransactionService {
    Transaction save(Transaction transaction);
}
