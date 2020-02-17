package rs.ac.uns.ftn.paypal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ViewPlanDTO {

    private String period;

    private Integer ucestalostPerioda;

    private BigDecimal cena;

}
