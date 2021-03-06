package rs.ac.uns.ftn.bitcoin.cmrs;

import lombok.Data;

import javax.validation.constraints.Digits;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
public class BitCoinPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer orderId;

    @Column(name="seller_email",nullable = false)
    private String sellerEmail;

    @Digits(integer = 10, fraction = 2)
    private BigDecimal amount;

    private String redirectUrl;

    private String status;

    @org.hibernate.annotations.Type(type="org.hibernate.type.UUIDCharType")
    @Column(name = "item_id",unique = false, nullable = false)
    private UUID itemId;

    @org.hibernate.annotations.Type(type="org.hibernate.type.UUIDCharType")
    @Column(name = "red_url_id",unique = false, nullable = false)
    private UUID redUrlUuid;

}
