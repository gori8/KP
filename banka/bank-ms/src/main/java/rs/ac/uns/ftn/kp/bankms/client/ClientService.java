package rs.ac.uns.ftn.kp.bankms.client;

import rs.ac.uns.ftn.kp.bankms.model.Client;

import java.util.List;

public interface ClientService {
    List<Client> findAll();

    Client findByMerchantId(String merchantId);

    Client getBySeller(String seller);

    Client save(Client client);
}
