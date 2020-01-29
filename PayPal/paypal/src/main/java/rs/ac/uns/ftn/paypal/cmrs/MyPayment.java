package rs.ac.uns.ftn.paypal.cmrs;


import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
public class MyPayment{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private BigDecimal amount;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Seller seller;

    private String paymentId;

    private String redirectUrl;

    @org.hibernate.annotations.Type(type="org.hibernate.type.UUIDCharType")
    @Column(name = "item_id", unique = false, nullable = false)
    private UUID itemId;

    private Boolean successful;

}
