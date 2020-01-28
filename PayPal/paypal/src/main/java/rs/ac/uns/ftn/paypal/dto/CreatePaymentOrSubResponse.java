package rs.ac.uns.ftn.paypal.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePaymentOrSubResponse {

    private String approvalUrl;

    private String id;
}
