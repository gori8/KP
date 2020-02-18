package rs.ac.uns.naucnacentrala.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.ac.uns.naucnacentrala.model.Link;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {

    @Query(value = "SELECT l FROM Link l LEFT OUTER JOIN l.casopis c WHERE c.id=?1 AND l.nacinPlacanja=?2")
    Link findOneByCasopisUuidAndNacinPlacanja(Long id, Long nacinPlacanja);
}
