package rs.ac.uns.ftn.paypal.cmrs;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SellerRepository extends JpaRepository<Seller,Long> {

    public Seller findByCasopisID(UUID casopisID);

}
