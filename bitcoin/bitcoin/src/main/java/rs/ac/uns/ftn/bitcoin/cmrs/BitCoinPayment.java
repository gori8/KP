package rs.ac.uns.ftn.bitcoin.cmrs;

import lombok.Data;

import javax.validation.constraints.Digits;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
public class BitCoinPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer orderId;

    @ManyToOne
    private Seller seller;

    @Digits(integer = 10, fraction = 2)
    private BigDecimal amount;

    private String redirectUrl;

    private String status;

}
