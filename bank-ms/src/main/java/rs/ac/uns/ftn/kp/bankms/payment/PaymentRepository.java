package rs.ac.uns.ftn.kp.bankms.payment;

import rs.ac.uns.ftn.kp.bankms.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Payment findOneByUrl(String url);
}
