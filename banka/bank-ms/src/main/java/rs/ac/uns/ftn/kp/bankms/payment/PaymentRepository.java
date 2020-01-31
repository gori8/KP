package rs.ac.uns.ftn.kp.bankms.payment;

import rs.ac.uns.ftn.kp.bankms.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.kp.bankms.model.PaymentStatus;

import java.util.List;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Payment findOneByUrl(String url);

    List<Payment> findAllByStatus(PaymentStatus status);

    List<Payment> findAllByStatusAndCheckedStatus(PaymentStatus status,Boolean checkedStatus);

}
