package com.example.bankms.client;

import com.example.bankms.model.Client;

import java.util.List;

public interface ClientService {
    List<Client> findAll();

    Client findByMerchantId(String merchantId);
}
