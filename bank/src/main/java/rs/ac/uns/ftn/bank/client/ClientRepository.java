package rs.ac.uns.ftn.bank.client;

import rs.ac.uns.ftn.bank.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findByMerchantId(String merchantId);
}
