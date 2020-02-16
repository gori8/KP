package rs.ac.uns.ftn.paypal.cmrs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SubPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String period;

    private Integer ucestalostPerioda;

    private BigDecimal cena;

    @org.hibernate.annotations.Type(type="org.hibernate.type.UUIDCharType")
    @Column(name = "plan_id", unique = false, nullable = false)
    private UUID planId;

}
