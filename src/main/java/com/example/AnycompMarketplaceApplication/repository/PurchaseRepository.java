package com.example.AnycompMarketplaceApplication.repository;

import com.example.AnycompMarketplaceApplication.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
}
