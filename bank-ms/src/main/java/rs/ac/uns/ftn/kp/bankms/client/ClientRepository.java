package rs.ac.uns.ftn.kp.bankms.client;

import rs.ac.uns.ftn.kp.bankms.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findByMerchantId(String merchantId);
    Client findByCasopisUuid(UUID id);
}
