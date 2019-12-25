package com.example.bank.client;

import com.example.bank.client.ClientService;
import com.example.bank.model.Client;
import com.example.bank.client.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    public Client findByMerchantId(String merchantId) {
        return clientRepository.findByMerchantId(merchantId);
    }
}
