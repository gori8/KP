package rs.ac.uns.ftn.kp.pcc.bank.info;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BankRepository extends JpaRepository<Bank, Long> {

    Bank findOneByBankCode(String bankcode);
}
