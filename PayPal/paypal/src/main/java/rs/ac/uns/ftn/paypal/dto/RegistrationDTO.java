package rs.ac.uns.ftn.paypal.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class RegistrationDTO {
    private UUID casopisID;
    private String email;
    private String merchant_id;

}
