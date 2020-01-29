package rs.ac.uns.ftn.kp.bankms.client;

import rs.ac.uns.ftn.kp.bankms.model.Seller;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SellerServiceImpl implements SellerService {
    private final SellerRepository sellerRepository;

    public SellerServiceImpl(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    @Override
    public List<Seller> findAll() {
        return sellerRepository.findAll();
    }

    @Override
    public Seller findByMerchantId(String merchantId) {
        return sellerRepository.findByMerchantId(merchantId);
    }

    @Override
    public Seller getBySeller(String seller) {
        return sellerRepository.findOneBySeller(seller);
    }

    @Override
    public Seller save(Seller seller) {
        return sellerRepository.save(seller);
    }


}
