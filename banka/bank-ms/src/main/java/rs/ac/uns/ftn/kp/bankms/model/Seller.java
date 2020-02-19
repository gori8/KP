package rs.ac.uns.ftn.kp.bankms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.url.ConverterAES;

import javax.persistence.*;

import javax.validation.constraints.Size;
import java.util.UUID;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "firstName", unique = false, nullable = false)
    private String firstName;

    @Column(name = "lastName", unique = false, nullable = false)
    private String lastName;

    @Convert(converter = ConverterAES.class)
    @Column(name = "merchantId", unique = false, nullable = false)
    private String merchantId;

    @Convert(converter = ConverterAES.class)
    @Column(name = "merchantPassword", unique = false, nullable = false)
    private String merchantPassword;

    @Column(name = "seller", unique = true, nullable = false)
    private String seller;

}
