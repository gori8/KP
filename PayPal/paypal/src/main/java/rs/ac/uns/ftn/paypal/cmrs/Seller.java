package rs.ac.uns.ftn.paypal.cmrs;

import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @org.hibernate.annotations.Type(type="org.hibernate.type.UUIDCharType")
    @Column(name = "casopisID", unique = true, nullable = false)
    private UUID casopisID;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "merchant_id", unique = true, nullable = false)
    private String merchant_id;

    @Column(name = "can_subscribe", nullable = false)
    private Boolean canSubscribe;

    @Column(name = "plan_id", unique = true)
    private String planId;

}
