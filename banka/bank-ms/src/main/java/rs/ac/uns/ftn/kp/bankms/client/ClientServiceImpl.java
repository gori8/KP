package rs.ac.uns.ftn.kp.bankms.client;

import rs.ac.uns.ftn.kp.bankms.model.Client;
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

    @Override
    public Client getBySeller(String seller) {
        return clientRepository.findOneBySeller(seller);
    }

    @Override
    public Client save(Client client) {
        return clientRepository.save(client);
    }


}
