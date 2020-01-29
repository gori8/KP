package rs.ac.uns.ftn.kp.bankms.client;

import rs.ac.uns.ftn.kp.bankms.model.Seller;

import java.util.List;

public interface SellerService {
    List<Seller> findAll();

    Seller findByMerchantId(String merchantId);

    Seller getBySeller(String seller);

    Seller save(Seller seller);
}
