package com.example.AnycompMarketplaceApplication.repository;

import com.example.AnycompMarketplaceApplication.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findBySellerId(Long sellerId);
}
