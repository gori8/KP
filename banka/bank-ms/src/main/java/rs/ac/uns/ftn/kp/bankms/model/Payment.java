package rs.ac.uns.ftn.kp.bankms.model;


import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount", unique = false, nullable = true)
    private BigDecimal amount;

    @Column(name = "url", unique = false, nullable = true)
    private String url;

    @Column(name = "itemUuid", unique = false, nullable = false)
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID itemUuid;

    @Column(name = "status", unique = false, nullable = false)
    private PaymentStatus status;

    @Column(name = "seller_email", unique = false, nullable = false)
    private String sellerEmail;

    @Column(name = "checked_status", unique = false, nullable = false)
    private Boolean checkedStatus=false;

}
