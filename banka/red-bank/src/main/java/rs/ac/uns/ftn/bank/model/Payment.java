package rs.ac.uns.ftn.bank.model;


import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url", unique = false, nullable = false)
    private String url;

    @Column(name = "amount", unique = false, nullable = true)
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Account merchant;

    @Column(name = "successUrl", unique = false, nullable = false)
    private String successUrl;

    @Column(name = "failedUrl", unique = false, nullable = false)
    private String failedUrl;

    @Column(name = "errorUrl", unique = false, nullable = false)
    private String errorUrl;

    @Column(name = "timeStamp", unique = false, nullable = false)
    private Date timeStamp;
}
