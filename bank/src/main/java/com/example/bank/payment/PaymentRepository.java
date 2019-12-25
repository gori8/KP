package com.example.bank.payment;

import com.example.bank.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByUrl(String url);
}
