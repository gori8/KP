package rs.ac.uns.ftn.bank.model;


import javax.persistence.*;
import java.math.BigDecimal;

@Entity
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Account getMerchant() {
        return merchant;
    }

    public void setMerchant(Account merchant) {
        this.merchant = merchant;
    }

    public String getSuccessUrl() {
        return successUrl;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public String getFailedUrl() {
        return failedUrl;
    }

    public void setFailedUrl(String failedUrl) {
        this.failedUrl = failedUrl;
    }

    public String getErrorUrl() {
        return errorUrl;
    }

    public void setErrorUrl(String errorUrl) {
        this.errorUrl = errorUrl;
    }

    public Payment() {
    }
}
