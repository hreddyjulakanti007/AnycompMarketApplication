package com.example.AnycompMarketplaceApplication.service;

import com.example.AnycompMarketplaceApplication.dto.PurchaseResponse;
import com.example.AnycompMarketplaceApplication.entity.Buyer;
import com.example.AnycompMarketplaceApplication.entity.Item;
import com.example.AnycompMarketplaceApplication.entity.Purchase;
import com.example.AnycompMarketplaceApplication.repository.BuyerRepository;
import com.example.AnycompMarketplaceApplication.repository.ItemRepository;
import com.example.AnycompMarketplaceApplication.repository.PurchaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final ItemRepository itemRepository;
    private final BuyerRepository buyerRepository;

    public PurchaseService(PurchaseRepository purchaseRepository, ItemRepository itemRepository, BuyerRepository buyerRepository) {
        this.purchaseRepository = purchaseRepository;
        this.itemRepository = itemRepository;
        this.buyerRepository = buyerRepository;
    }

    public PurchaseResponse createPurchase(Long buyerId, Long itemId, int quantity) {
        Buyer buyer = buyerRepository.findById(buyerId)
                .orElseThrow(() -> new IllegalArgumentException("Buyer not found with ID: " + buyerId));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found with ID: " + itemId));

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        if (item.getQuantity() < quantity) {
            throw new IllegalArgumentException("Insufficient item quantity. Available: " + item.getQuantity());
        }

        // Reduce item stock
        item.setQuantity(item.getQuantity() - quantity);
        itemRepository.save(item);

        // Create and save purchase
        Purchase purchase = new Purchase();
        purchase.setBuyer(buyer);
        purchase.setItem(item);
        purchase.setQuantity(quantity);
        purchase.setPurchaseDate(new Timestamp(System.currentTimeMillis()));

        Purchase savedPurchase = purchaseRepository.save(purchase);

        // Map to PurchaseResponse
        PurchaseResponse response = new PurchaseResponse();
        response.setPurchaseId(savedPurchase.getId());
        response.setBuyerId(buyer.getId());
        response.setBuyerName(buyer.getName());
        response.setItemId(item.getId());
        response.setItemName(item.getName());
        response.setQuantity(savedPurchase.getQuantity());
        response.setPurchaseDate(savedPurchase.getPurchaseDate());

        return response;
    }

    // Pagination method for purchases
    public Page<PurchaseResponse> getAllPurchases(Pageable pageable) {
        return purchaseRepository.findAll(pageable)
                .map(purchase -> {
                    PurchaseResponse response = new PurchaseResponse();
                    response.setPurchaseId(purchase.getId());
                    response.setBuyerId(purchase.getBuyer().getId());
                    response.setBuyerName(purchase.getBuyer().getName());
                    response.setItemId(purchase.getItem().getId());
                    response.setItemName(purchase.getItem().getName());
                    response.setQuantity(purchase.getQuantity());
                    response.setPurchaseDate(purchase.getPurchaseDate());
                    return response;
                });
    }
}
