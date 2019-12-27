package rs.ac.uns.ftn.bitcoin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CoinGateResponse {
    private Integer id;

    private String status;

    @JsonProperty(value = "price_amount")
    private String priceAmount;

    @JsonProperty(value = "payment_url")
    private String paymentUrl;

}
