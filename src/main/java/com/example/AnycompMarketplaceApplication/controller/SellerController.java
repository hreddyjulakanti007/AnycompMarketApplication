package com.example.AnycompMarketplaceApplication.controller;

import com.example.AnycompMarketplaceApplication.entity.Seller;
import com.example.AnycompMarketplaceApplication.service.SellerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/sellers")
@Tag(name = "Seller", description = "Seller Management APIs")
public class SellerController {

    private final SellerService sellerService;

    @Autowired
    public SellerController(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @GetMapping
    @Operation(summary = "Get all sellers", description = "Returns a list of all sellers")
    @ApiResponse(responseCode = "200", description = "List of sellers",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Seller.class)))
    public List<Seller> getAllSellers() {
        return sellerService.getAllSellers();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get seller by ID", description = "Returns a seller based on ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Seller found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Seller.class))),
            @ApiResponse(responseCode = "404", description = "Seller not found", content = @Content)
    })
    public ResponseEntity<Seller> getSellerById(@PathVariable Long id) {
        Optional<Seller> seller = sellerService.getSellerById(id);
        return seller.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    @Operation(summary = "Create a new seller", description = "Creates a new seller entity")
    @ApiResponse(responseCode = "201", description = "Seller created",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Seller.class)))
    public ResponseEntity<Seller> createSeller(@RequestBody Seller seller) {
        Seller createdSeller = sellerService.createSeller(seller);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSeller);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update seller", description = "Updates a seller by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Seller updated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Seller.class))),
            @ApiResponse(responseCode = "404", description = "Seller not found", content = @Content)
    })
    public ResponseEntity<Seller> updateSeller(@PathVariable Long id, @RequestBody Seller seller) {
        if (sellerService.getSellerById(id).isPresent()) {
            seller.setId(id);
            Seller updatedSeller = sellerService.updateSeller(id, seller);
            return ResponseEntity.ok(updatedSeller);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete seller", description = "Deletes a seller by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Seller deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "Seller not found", content = @Content)
    })
    public ResponseEntity<Void> deleteSeller(@PathVariable Long id) {
        if (sellerService.getSellerById(id).isPresent()) {
            sellerService.deleteSeller(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
