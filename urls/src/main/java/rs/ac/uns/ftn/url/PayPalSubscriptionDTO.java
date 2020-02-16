package rs.ac.uns.ftn.url;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PayPalSubscriptionDTO {

    private String period;

    private Integer ucestalostPerioda;

    private BigDecimal cena;

    private String redirectUrl;

    private String planUuid;
}
