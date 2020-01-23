package rs.ac.uns.ftn.kp.bankms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;

import javax.validation.constraints.Size;
import java.util.UUID;


@Entity
@Data
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "firstName", unique = false, nullable = false)
    private String firstName;

    @Column(name = "lastName", unique = false, nullable = false)
    private String lastName;

    @Size(max = 30)
    @Column(name = "merchantId", unique = false, nullable = false)
    private String merchantId;

    @Column(name = "casopisUuid", unique = false, nullable = false)
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID casopisUuid;

    @Size(max = 100)
    @Column(name = "merchantPassword", unique = false, nullable = false)
    private String merchantPassword;


}
