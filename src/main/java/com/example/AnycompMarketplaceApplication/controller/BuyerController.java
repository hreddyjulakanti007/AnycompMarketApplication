package com.example.AnycompMarketplaceApplication.controller;

import com.example.AnycompMarketplaceApplication.entity.Buyer;
import com.example.AnycompMarketplaceApplication.service.BuyerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping({"/buyers", "/"})
@Tag(name = "Buyer Management", description = "CRUD operations for buyers")
public class BuyerController {

    private final BuyerService buyerService;

    public BuyerController(BuyerService buyerService) {
        this.buyerService = buyerService;
    }

    @GetMapping
    @Operation(summary = "Get all buyers (paginated)", description = "Returns a paginated list of buyers")
    public Page<Buyer> getAllBuyers(@Parameter(hidden = true) Pageable pageable) {
        return buyerService.getAllBuyers(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get buyer by ID", description = "Returns a single buyer by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the buyer", content = @Content(schema = @Schema(implementation = Buyer.class))),
            @ApiResponse(responseCode = "404", description = "Buyer not found", content = @Content)
    })
    public ResponseEntity<Buyer> getBuyerById(@PathVariable Long id) {
        Optional<Buyer> buyer = buyerService.getBuyerById(id);
        return buyer.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new buyer", description = "Adds a new buyer to the system")
    @ApiResponse(responseCode = "200", description = "Buyer created", content = @Content(schema = @Schema(implementation = Buyer.class)))
    public Buyer createBuyer(@RequestBody Buyer buyer) {
        return buyerService.createBuyer(buyer);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a buyer", description = "Updates an existing buyer's details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Buyer updated", content = @Content(schema = @Schema(implementation = Buyer.class))),
            @ApiResponse(responseCode = "404", description = "Buyer not found", content = @Content)
    })
    public ResponseEntity<Buyer> updateBuyer(@PathVariable Long id, @RequestBody Buyer buyer) {
        Optional<Buyer> existingBuyer = buyerService.getBuyerById(id);
        if (existingBuyer.isPresent()) {
            Buyer updatedBuyer = buyerService.updateBuyer(id, buyer);
            return ResponseEntity.ok(updatedBuyer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a buyer", description = "Deletes a buyer by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Buyer deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "Buyer not found", content = @Content)
    })
    public ResponseEntity<Void> deleteBuyer(@PathVariable Long id) {
        Optional<Buyer> existingBuyer = buyerService.getBuyerById(id);
        if (existingBuyer.isPresent()) {
            buyerService.deleteBuyer(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
