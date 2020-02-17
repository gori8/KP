package rs.ac.uns.ftn.fourthms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationDTO {

    private String firstName;
    private String lastName;
    private String merchantId;
    private String sellerEmail;
}
