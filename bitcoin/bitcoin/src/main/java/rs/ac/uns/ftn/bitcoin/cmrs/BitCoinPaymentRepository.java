package rs.ac.uns.ftn.bitcoin.cmrs;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BitCoinPaymentRepository extends JpaRepository<BitCoinPayment,Long> {
}
