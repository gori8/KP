package rs.ac.uns.ftn.paypal.cmrs;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller,Long> {

    public Seller findByCasopisID(Long id);

}
