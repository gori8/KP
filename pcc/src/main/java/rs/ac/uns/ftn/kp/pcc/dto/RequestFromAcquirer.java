package rs.ac.uns.ftn.kp.pcc.dto;

import lombok.Data;

import java.util.Date;

@Data
public class RequestFromAcquirer {

    private String acquirerOrderId;
    private Date acquirerTimestamp;
    private String pan;
    private Integer securityCode;
    private String holderName;
    private Date validTo;

}
