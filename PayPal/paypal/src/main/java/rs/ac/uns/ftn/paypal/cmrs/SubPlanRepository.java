package rs.ac.uns.ftn.paypal.cmrs;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SubPlanRepository extends JpaRepository<SubPlan,Long> {

    SubPlan findOneByPlanId(UUID uuid);

}
