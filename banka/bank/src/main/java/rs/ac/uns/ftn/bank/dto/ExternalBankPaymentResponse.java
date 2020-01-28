package rs.ac.uns.ftn.bank.dto;

public class ExternalBankPaymentResponse {

    private Long id;
    private String url;

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

    public ExternalBankPaymentResponse() {
    }

    @Override
    public String toString() {
        return "ExternalBankPaymentResponse{" +
                "id=" + id +
                ", url='" + url + '\'' +
                '}';
    }
}
