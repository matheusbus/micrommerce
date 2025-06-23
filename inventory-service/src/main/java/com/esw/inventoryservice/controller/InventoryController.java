package com.esw.inventoryservice.controller;

import com.esw.inventoryservice.dto.DeduceStockRequest;
import com.esw.inventoryservice.dto.ResetStockRequest;
import com.esw.inventoryservice.dto.AddStockRequest;
import com.esw.inventoryservice.dto.ReserveStockRequest;
import com.esw.inventoryservice.model.Inventory;
import com.esw.inventoryservice.usecase.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final AddStockUseCase addStockUseCase;
    private final DeduceStockUseCase deduceStockUseCase;
    private final ReserveStockUseCase reserveStockUseCase;
    private final ResetStockUseCase resetStockUseCase;
    private final GetStockByProductIdUseCase getStockByProductIdUseCase;
    private final GetAllInventoryUseCase getAllInventoryUseCase;

    public InventoryController(AddStockUseCase addStockUseCase, DeduceStockUseCase deduceStockUseCase, ReserveStockUseCase reserveStockUseCase, ResetStockUseCase resetStockUseCase, GetStockByProductIdUseCase getStockByProductIdUseCase, GetAllInventoryUseCase getAllInventoryUseCase) {
        this.addStockUseCase = addStockUseCase;
        this.deduceStockUseCase = deduceStockUseCase;
        this.reserveStockUseCase = reserveStockUseCase;
        this.resetStockUseCase = resetStockUseCase;
        this.getStockByProductIdUseCase = getStockByProductIdUseCase;
        this.getAllInventoryUseCase = getAllInventoryUseCase;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Inventory>> getAllInventory() {
        return ResponseEntity.ok(getAllInventoryUseCase.handle());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Inventory> getStock(@PathVariable Long productId) {
        return ResponseEntity.ok(getStockByProductIdUseCase.handle(productId));
    }

    @PostMapping("/add")
    public ResponseEntity<Inventory> addStock(@RequestBody @Valid AddStockRequest request) {
        return ResponseEntity.ok(addStockUseCase.handle(request));
    }

    @PostMapping("/reserve")
    public ResponseEntity<Inventory> reserveStock(@RequestBody @Valid ReserveStockRequest request) {
        return ResponseEntity.ok(reserveStockUseCase.handle(request));
    }

    @PostMapping("/deduce")
    public ResponseEntity<Inventory> deduceStock(@RequestBody @Valid DeduceStockRequest request) {
        return ResponseEntity.ok(deduceStockUseCase.handle(request));
    }

    @PostMapping("/reset")
    public ResponseEntity<Inventory> resetStock(@RequestBody @Valid ResetStockRequest request) {
        return ResponseEntity.ok(resetStockUseCase.handle(request));
    }
}
