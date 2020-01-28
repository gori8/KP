package rs.ac.uns.ftn.url;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.TimeZone;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PccDTO {

    private String acquirerOrderId;

    private Date acquirerTimestamp;

    private String pan;

    private Integer securityCode;

    private String holderName;

    private String validTo;

    private String bankCode;

    private BigDecimal amount;

}

