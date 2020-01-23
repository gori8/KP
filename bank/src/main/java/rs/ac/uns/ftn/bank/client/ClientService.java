package rs.ac.uns.ftn.bank.client;

import rs.ac.uns.ftn.bank.model.Client;

import java.util.List;

public interface ClientService {
    List<Client> findAll();

    Client findByMerchantId(String merchantId);
}
