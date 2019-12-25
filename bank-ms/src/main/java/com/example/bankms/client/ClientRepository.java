package com.example.bankms.client;

import com.example.bankms.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findByMerchantId(String merchantId);
    Client findByCasopisId(Long id);
}
