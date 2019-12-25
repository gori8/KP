package com.example.bank.account;

import com.example.bank.model.Account;

import java.util.List;

public interface AccountService {
    Account getOne(Long id);

    List<Account> findAll();

    Account save(Account account);
}
