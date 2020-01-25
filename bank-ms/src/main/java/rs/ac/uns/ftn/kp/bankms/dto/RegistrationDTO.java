package rs.ac.uns.ftn.kp.bankms.dto;

import lombok.Data;

@Data
public class RegistrationDTO {
    private String uuid;
    private String firstName;
    private String lastName;
    private String merchantId;
    private String merchantPassword;
}
