package com.example.bankms.model;


import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount", unique = false, nullable = true)
    private BigDecimal amount;

    @Column(name = "url", unique = false, nullable = false)
    private String url;

    @Column(name = "casopisUuid", unique = false, nullable = false)
    private String casopisUuid;

    @Column(name = "placeno", unique = false, nullable = false)
    private Boolean placeno;

    public String getCasopisUuid() {
        return casopisUuid;
    }

    public Boolean getPlaceno() {
        return placeno;
    }

    public void setPlaceno(Boolean placeno) {
        this.placeno = placeno;
    }

    public void setCasopisUuid(String casopisUuid) {
        this.casopisUuid = casopisUuid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Payment() {
    }
}
