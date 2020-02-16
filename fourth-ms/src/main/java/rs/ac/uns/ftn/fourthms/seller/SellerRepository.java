package rs.ac.uns.ftn.fourthms.seller;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.fourthms.seller.Seller;


public interface SellerRepository extends JpaRepository<Seller, Long>{

    Seller findSellerByEmail(String email);
}
