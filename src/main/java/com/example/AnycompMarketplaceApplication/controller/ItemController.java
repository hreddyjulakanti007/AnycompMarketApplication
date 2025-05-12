package com.example.AnycompMarketplaceApplication.controller;

import com.example.AnycompMarketplaceApplication.dto.ItemRequestDTO;
import com.example.AnycompMarketplaceApplication.dto.ItemResponseDTO;
import com.example.AnycompMarketplaceApplication.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/items")
@Tag(name = "Item", description = "Item Management APIs")
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    @Operation(summary = "Get all items (paginated)", description = "Returns a paginated list of items")
    @ApiResponse(responseCode = "200", description = "Paginated list of items retrieved",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ItemResponseDTO.class)))
    public Page<ItemResponseDTO> getAllItems(@Parameter(hidden = true) Pageable pageable) {
        return itemService.getAllItems(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get item by ID", description = "Returns an item by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item found", content = @Content(schema = @Schema(implementation = ItemResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Item not found", content = @Content)
    })
    public ResponseEntity<ItemResponseDTO> getItemById(
            @Parameter(description = "ID of the item to retrieve") @PathVariable Long id) {
        return itemService.getItemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/sellers/{sellerId}")
    @Operation(summary = "Get items by seller ID", description = "Returns all items listed by a specific seller")
    @ApiResponse(responseCode = "200", description = "List of seller's items",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ItemResponseDTO.class)))
    public ResponseEntity<Page<ItemResponseDTO>> getItemsBySeller(
            @Parameter(description = "Seller ID to retrieve items for") @PathVariable Long sellerId,
            @Parameter(hidden = true) Pageable pageable) {
        return ResponseEntity.ok(itemService.getItemsBySellerId(sellerId, pageable));
    }

    @PostMapping("/sellers/{sellerId}")
    @Operation(summary = "Add new item to seller", description = "Creates a new item under the given seller")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Item created", content = @Content(schema = @Schema(implementation = ItemResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Seller not found", content = @Content)
    })
    public ResponseEntity<ItemResponseDTO> addItemToSeller(
            @Parameter(description = "ID of the seller") @PathVariable Long sellerId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Item details", required = true,
                    content = @Content(schema = @Schema(implementation = ItemRequestDTO.class)))
            @RequestBody ItemRequestDTO itemDTO) {
        try {
            ItemResponseDTO createdItem = itemService.addItem(sellerId, itemDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update item", description = "Updates item information")
    @ApiResponse(responseCode = "200", description = "Item updated", content = @Content(schema = @Schema(implementation = ItemResponseDTO.class)))
    public ResponseEntity<ItemResponseDTO> updateItem(
            @Parameter(description = "Item ID") @PathVariable Long id,
            @RequestBody ItemRequestDTO itemDTO) {
        ItemResponseDTO updatedItem = itemService.updateItem(id, itemDTO);
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete item", description = "Deletes item by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Item deleted"),
            @ApiResponse(responseCode = "404", description = "Item not found", content = @Content)
    })
    public ResponseEntity<Void> deleteItem(
            @Parameter(description = "Item ID to delete") @PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}
