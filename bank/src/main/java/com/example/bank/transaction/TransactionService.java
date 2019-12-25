package com.example.bank.transaction;

import com.example.bank.model.Transaction;

public interface TransactionService {
    Transaction save(Transaction transaction);
}
