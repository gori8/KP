package rs.ac.uns.ftn.bank.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteTransactionResponse {

    private Boolean successful;
    private String url;

}
