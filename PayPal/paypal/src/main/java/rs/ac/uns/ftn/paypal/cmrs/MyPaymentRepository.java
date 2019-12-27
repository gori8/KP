package rs.ac.uns.ftn.paypal.cmrs;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MyPaymentRepository extends JpaRepository<MyPayment,Long> {

    public MyPayment findByPaymentId(String paymentId);

}
