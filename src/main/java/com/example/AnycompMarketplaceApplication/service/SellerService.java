package com.example.AnycompMarketplaceApplication.service;

import com.example.AnycompMarketplaceApplication.entity.Seller;
import com.example.AnycompMarketplaceApplication.repository.SellerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SellerService {

    private final SellerRepository sellerRepository;

    public SellerService(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    public Page<Seller> getAllSellers(Pageable pageable) {
        return sellerRepository.findAll(pageable);
    }

    public Optional<Seller> getSellerById(Long id) {
        return sellerRepository.findById(id);
    }

    public Seller createSeller(Seller seller) {
        return sellerRepository.save(seller);
    }

    public Seller updateSeller(Long id, Seller seller) {
        seller.setId(id);
        return sellerRepository.save(seller);
    }

    public void deleteSeller(Long id) {
        sellerRepository.deleteById(id);
    }
}
