package rs.ac.uns.ftn.bank.account;

import rs.ac.uns.ftn.bank.model.Account;

import java.util.List;

public interface AccountService {
    Account getOne(Long id);

    List<Account> findAll();

    Account save(Account account);
}
