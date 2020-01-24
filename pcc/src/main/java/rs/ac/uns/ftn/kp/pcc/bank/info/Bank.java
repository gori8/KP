package rs.ac.uns.ftn.kp.pcc.bank.info;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Bank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bankCode", unique = false, nullable = false)
    private String bankCode;

    @Column(name = "bankUrl", unique = false, nullable = false)
    private String bankUrl;
}
