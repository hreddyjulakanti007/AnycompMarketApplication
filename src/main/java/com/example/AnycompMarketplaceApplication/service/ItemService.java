package com.example.AnycompMarketplaceApplication.service;

import com.example.AnycompMarketplaceApplication.dto.ItemRequestDTO;
import com.example.AnycompMarketplaceApplication.dto.ItemResponseDTO;
import com.example.AnycompMarketplaceApplication.entity.Item;
import com.example.AnycompMarketplaceApplication.entity.Seller;
import com.example.AnycompMarketplaceApplication.repository.ItemRepository;
import com.example.AnycompMarketplaceApplication.repository.SellerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final SellerRepository sellerRepository;

    public ItemService(ItemRepository itemRepository, SellerRepository sellerRepository) {
        this.itemRepository = itemRepository;
        this.sellerRepository = sellerRepository;
    }

    // Convert Entity to DTO
    private ItemResponseDTO convertToDTO(Item item) {
        ItemResponseDTO dto = new ItemResponseDTO();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setPrice(item.getPrice());
        dto.setSellerId(item.getSeller().getId());
        return dto;
    }

    // Convert DTO to Entity
    private Item convertToEntity(ItemRequestDTO dto) {
        Item item = new Item();
        item.setName(dto.getName());
        item.setPrice(dto.getPrice());
        return item;
    }

    public Page<ItemResponseDTO> getAllItems(Pageable pageable) {
        return itemRepository.findAll(pageable).map(this::convertToDTO);
    }

    public Optional<ItemResponseDTO> getItemById(Long id) {
        return itemRepository.findById(id).map(this::convertToDTO);
    }

    public Page<ItemResponseDTO> getItemsBySellerId(Long sellerId, Pageable pageable) {
        return itemRepository.findBySellerId(sellerId, pageable).map(this::convertToDTO);
    }

    public ItemResponseDTO addItem(Long sellerId, ItemRequestDTO itemDTO) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new RuntimeException("Seller not found with ID: " + sellerId));

        Item item = convertToEntity(itemDTO);
        item.setSeller(seller);
        Item savedItem = itemRepository.save(item);

        return convertToDTO(savedItem);
    }

    public ItemResponseDTO updateItem(Long id, ItemRequestDTO itemDTO) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with ID: " + id));

        item.setName(itemDTO.getName());
        item.setPrice(itemDTO.getPrice());

        Item updatedItem = itemRepository.save(item);
        return convertToDTO(updatedItem);
    }

    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }
}
