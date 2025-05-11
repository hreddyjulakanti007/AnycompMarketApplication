package com.example.AnycompMarketplaceApplication.repository;

import com.example.AnycompMarketplaceApplication.entity.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuyerRepository extends JpaRepository<Buyer, Long> {
}
