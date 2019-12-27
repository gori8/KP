package rs.ac.uns.ftn.bitcoin.cmrs;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Data
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @org.hibernate.annotations.Type(type="org.hibernate.type.UUIDCharType")
    private UUID uuid;


}