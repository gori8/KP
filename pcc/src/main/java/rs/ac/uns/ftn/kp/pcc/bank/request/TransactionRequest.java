package rs.ac.uns.ftn.kp.pcc.bank.request;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class TransactionRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "acquirerOrderId", unique = false, nullable = false)
    private String acquirerOrderId;

    @Column(name = "acquirerTimestamp", unique = false, nullable = false)
    private Date acquirerTimestamp;

    @Column(name = "pan", unique = false, nullable = false)
    private String pan;

    @Column(name = "securityCode", unique = false, nullable = false)
    private Integer securityCode;

    @Column(name = "holderName", unique = false, nullable = false)
    private String holderName;

    @Column(name = "validTo", unique = false, nullable = false)
    private Date validTo;

}
