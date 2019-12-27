package rs.ac.uns.ftn.bitcoin.cmrs;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SellerRepository extends JpaRepository<Seller,Long> {


    public Seller findByToken(String token);

    public Seller findByUuid(UUID uuid);

}
