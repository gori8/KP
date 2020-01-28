package rs.ac.uns.ftn.kp.bankms.model;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class KpSeller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @OneToMany(mappedBy = "seller")
    private Set<Client> bankClients = new HashSet<>();

}
