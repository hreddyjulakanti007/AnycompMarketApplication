package com.example.AnycompMarketplaceApplication.repository;

import com.example.AnycompMarketplaceApplication.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, Long> {
}
