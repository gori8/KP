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

    @Column(name = "seller_email", unique = true, nullable = false)
    private String sellerEmail;

    @Column(name = "paypal_email", unique = true, nullable = false)
    private String paypalEmail;

    @Column(name = "merchant_id", unique = true, nullable = false)
    private String merchant_id;

    @Column(name = "can_subscribe", nullable = false)
    private Boolean canSubscribe;


}
