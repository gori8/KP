package rs.ac.uns.ftn.kp.bankms.client;

import rs.ac.uns.ftn.kp.bankms.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, Long> {
    Seller findByMerchantId(String merchantId);

    Seller findOneBySeller(String seller);
}
