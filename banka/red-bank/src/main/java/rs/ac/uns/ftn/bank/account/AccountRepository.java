package rs.ac.uns.ftn.bank.account;

import rs.ac.uns.ftn.bank.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

}
