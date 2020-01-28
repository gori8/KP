package rs.ac.uns.ftn.kp.bankms.seller;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.kp.bankms.model.KpSeller;

public interface KpSellerRepository extends JpaRepository<KpSeller,Long> {

    KpSeller findOneByEmail(String email);

}
