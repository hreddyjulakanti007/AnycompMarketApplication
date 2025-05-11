package com.example.AnycompMarketplaceApplication.controller;

import com.example.AnycompMarketplaceApplication.dto.PurchaseRequest;
import com.example.AnycompMarketplaceApplication.dto.PurchaseResponse;
import com.example.AnycompMarketplaceApplication.service.PurchaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/purchase")
@Tag(name = "Purchase", description = "Operations related to item purchases")
public class PurchaseController {

    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping
    @Operation(
            summary = "Create a purchase",
            description = "Executes a purchase by a buyer for a specified item and quantity"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Purchase created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PurchaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or insufficient quantity", content = @Content)
    })
    public ResponseEntity<PurchaseResponse> createPurchase(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Purchase request with buyerId, itemId, and quantity",
                    required = true,
                    content = @Content(schema = @Schema(implementation = PurchaseRequest.class))
            )
            @RequestBody PurchaseRequest purchaseRequest) {

        try {
            PurchaseResponse response = purchaseService.createPurchase(
                    purchaseRequest.getBuyerId(),
                    purchaseRequest.getItemId(),
                    purchaseRequest.getQuantity()
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
