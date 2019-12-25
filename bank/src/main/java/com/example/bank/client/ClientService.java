package com.example.bank.client;

import com.example.bank.model.Client;

import java.util.List;

public interface ClientService {
    List<Client> findAll();

    Client findByMerchantId(String merchantId);
}
