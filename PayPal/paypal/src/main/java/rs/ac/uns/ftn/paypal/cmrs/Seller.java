package rs.ac.uns.ftn.paypal.cmrs;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @org.hibernate.annotations.Type(type="org.hibernate.type.UUIDBinaryType")
    @Column(name = "casopisID", unique = true, nullable = false)
    private Long casopisID;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "merchant_id", unique = true, nullable = false)
    private String merchant_id;

}
