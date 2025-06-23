package com.esw.inventoryservice.usecase;

import com.esw.inventoryservice.dto.ResetStockRequest;
import com.esw.inventoryservice.exception.NotFoundException;
import com.esw.inventoryservice.model.Inventory;
import com.esw.inventoryservice.repository.InventoryRepository;
import org.springframework.stereotype.Service;

@Service
public class ResetStockUseCase {

    private final InventoryRepository repository;

    public ResetStockUseCase(InventoryRepository repository) {
        this.repository = repository;
    }

    public Inventory handle(ResetStockRequest request) {
        Inventory inv = repository.findByProductId(request.productId())
                .orElseThrow(() -> new NotFoundException(Inventory.class, "productId", request.productId().toString()));

        inv.reset();

        return repository.save(inv);
    }
}
