package rs.ac.uns.ftn.bitcoin.cmrs;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller,Long> {

    public Seller findByEmail(String email);

    public Seller findByToken(String token);

}
