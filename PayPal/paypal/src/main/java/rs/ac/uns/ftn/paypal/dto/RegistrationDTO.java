package rs.ac.uns.ftn.paypal.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class RegistrationDTO {

    private String uuid;
    private String email;
    private String merchantId;
    private String sellerEmail;
}
