package com.example.AnycompMarketplaceApplication.dto;

import lombok.Data;

@Data
public class ItemResponseDTO {
    private Long id;
    private String name;
    private Double price;
    private Long sellerId;
}
