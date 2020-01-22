package rs.ac.uns.ftn.bank.payment;

import rs.ac.uns.ftn.bank.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByUrl(String url);
}
