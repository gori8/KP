package rs.ac.uns.ftn.bitcoin.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class CoinGateRequest {
    String token;

    BigDecimal amount;

    String successUrl;

    String cancelUrl;

    String currency;

    String title;

    String redirectUrl;

    Long paymentId;

    String email;

}
