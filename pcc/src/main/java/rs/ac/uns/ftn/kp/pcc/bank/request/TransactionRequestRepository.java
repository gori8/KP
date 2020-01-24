package rs.ac.uns.ftn.kp.pcc.bank.request;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRequestRepository extends JpaRepository<TransactionRequest, Long> {
}
