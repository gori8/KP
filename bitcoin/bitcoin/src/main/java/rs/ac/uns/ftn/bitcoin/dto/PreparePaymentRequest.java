package rs.ac.uns.ftn.bitcoin.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PreparePaymentRequest {

    private String uuid;

    private BigDecimal amount;

    private String redirectUrl;
}
