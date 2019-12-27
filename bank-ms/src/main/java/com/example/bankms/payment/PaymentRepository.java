package com.example.bankms.payment;

import com.example.bankms.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Payment findOneByUrl(String url);
}
