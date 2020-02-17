package rs.ac.uns.ftn.fourthms.seller;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "firstName", unique = true, nullable = false)
    private String firstName;

    @Column(name = "lastName", unique = true, nullable = false)
    private String lastName;

    @Column(name = "merchantId", unique = true, nullable = false)
    private String merchantId;

}