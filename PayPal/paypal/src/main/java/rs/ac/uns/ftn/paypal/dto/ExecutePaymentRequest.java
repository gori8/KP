package rs.ac.uns.ftn.paypal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExecutePaymentRequest {

    private String paymentID;
    private String payerID;
    private String token;

}
