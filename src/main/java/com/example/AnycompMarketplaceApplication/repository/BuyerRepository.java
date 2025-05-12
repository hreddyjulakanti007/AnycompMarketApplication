package com.example.AnycompMarketplaceApplication.repository;

import com.example.AnycompMarketplaceApplication.entity.Buyer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuyerRepository extends JpaRepository<Buyer, Long> {
    Page<Buyer> findAll(Pageable pageable);  // Adding pagination to find all buyers
}
