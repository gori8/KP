package rs.ac.uns.ftn.bank.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PccDTO {
    private String acquirerOrderId;
    private Date acquirerTimestamp;
    private String pan;
    private Integer securityCode;
    private String holderName;
    private Date validTo;
    private BigDecimal amount;
    private String bankCode;
}
