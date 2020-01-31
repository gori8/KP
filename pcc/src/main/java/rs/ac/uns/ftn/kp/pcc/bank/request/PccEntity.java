package rs.ac.uns.ftn.kp.pcc.bank.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ac.uns.ftn.url.ConverterAES;
import rs.ac.uns.ftn.url.TransactionStatus;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PccEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = ConverterAES.class)
    private String acquirerBankCode;

    private Long acquirerOrderId;
    private Date acquirerTimeStamp;
    private String issuerBankCode;
    private Long issuerOrderId;
    private Date issuerTimeStamp;
    private TransactionStatus status;
    private BigDecimal amount;

}
