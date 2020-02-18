package rs.ac.uns.naucnacentrala.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rs.ac.uns.naucnacentrala.model.Plan;

import java.util.List;
import java.util.UUID;

public interface PlanRepository extends JpaRepository<Plan, Long> {

    Plan findOneByUuid(UUID uuid);

    @Query(value = "SELECT p FROM Plan p WHERE p.casopis.id=?1")
    List<Plan> findAllByCasopisId(Long casopisId);
}