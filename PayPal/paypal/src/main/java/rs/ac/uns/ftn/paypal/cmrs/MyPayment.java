package rs.ac.uns.ftn.paypal.cmrs;


import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

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

    private Boolean successful;

}
