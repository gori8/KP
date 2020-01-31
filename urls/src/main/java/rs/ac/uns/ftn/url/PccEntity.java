package rs.ac.uns.ftn.url;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PccEntity {

    private String acquirerBankCode;
    private Long acquirerOrderId;
    private Date acquirerTimeStamp;
    private String issuerBankCode;
    private Long issuerOrderId;
    private Date issuerTimeStamp;
    private TransactionStatus status;
    private BigDecimal amount;

}
