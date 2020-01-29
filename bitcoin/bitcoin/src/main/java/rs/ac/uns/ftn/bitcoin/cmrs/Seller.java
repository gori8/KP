package rs.ac.uns.ftn.bitcoin.cmrs;

import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @Column(name="seller_email" , unique = true, nullable = false)
    private String sellerEmail;

}