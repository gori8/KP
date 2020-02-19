package rs.ac.uns.ftn.paypal.cmrs;

import lombok.Data;
import rs.ac.uns.ftn.url.ConverterAES;

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

    @Convert(converter = ConverterAES.class)
    @Column(name = "paypal_email", unique = true, nullable = false)
    private String paypalEmail;

    @Convert(converter = ConverterAES.class)
    @Column(name = "merchant_id", unique = true, nullable = false)
    private String merchant_id;

    @Column(name = "can_subscribe", nullable = false)
    private Boolean canSubscribe;


}
