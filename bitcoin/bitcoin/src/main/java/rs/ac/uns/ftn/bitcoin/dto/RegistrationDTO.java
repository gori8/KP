package rs.ac.uns.ftn.bitcoin.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class RegistrationDTO {

    private String uuid;
    private String token;
    private String sellerEmail;

}
