package rs.ac.uns.ftn.paypal.cmrs;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MyPaymentRepository extends JpaRepository<MyPayment,Long> {

    MyPayment findByPaymentId(String paymentId);

    List<MyPayment> findAllByStatus(PayPalPaymentStatus status);

    List<MyPayment> findAllByStatusAndCheckedStatus(PayPalPaymentStatus status, Boolean checkedStatus);


}
