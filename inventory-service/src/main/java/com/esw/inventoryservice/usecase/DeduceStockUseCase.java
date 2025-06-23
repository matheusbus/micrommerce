package com.esw.inventoryservice.usecase;

import com.esw.inventoryservice.dto.DeduceStockRequest;
import com.esw.inventoryservice.exception.NotFoundException;
import com.esw.inventoryservice.model.Inventory;
import com.esw.inventoryservice.repository.InventoryRepository;
import org.springframework.stereotype.Service;

@Service
public class DeduceStockUseCase {

    private final InventoryRepository repository;

    public DeduceStockUseCase(InventoryRepository repository) {
        this.repository = repository;
    }

    public Inventory handle(DeduceStockRequest request) {
        Inventory inv = repository.findByProductId(request.productId())
                .orElseThrow(() -> new NotFoundException(Inventory.class, "productId", request.productId().toString()));

        inv.setAvailable(inv.getAvailable() - request.quantity());

        return repository.save(inv);
    }
}