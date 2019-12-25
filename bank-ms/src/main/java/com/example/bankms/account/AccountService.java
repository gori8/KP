package com.example.bankms.account;

import com.example.bankms.model.Account;

import java.util.List;

public interface AccountService {
    Account getOne(Long id);

    List<Account> findAll();

    Account save(Account account);
}
