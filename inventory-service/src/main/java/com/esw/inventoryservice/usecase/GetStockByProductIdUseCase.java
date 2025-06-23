package com.esw.inventoryservice.usecase;

import com.esw.inventoryservice.exception.NotFoundException;
import com.esw.inventoryservice.model.Inventory;
import com.esw.inventoryservice.repository.InventoryRepository;
import org.springframework.stereotype.Service;

@Service
public class GetStockByProductIdUseCase {

    private final InventoryRepository repository;

    public GetStockByProductIdUseCase(InventoryRepository repository) {
        this.repository = repository;
    }

    public Inventory handle(Long productId) {
        return repository.findByProductId(productId)
                .orElseThrow(() -> new NotFoundException(Inventory.class, "productId", productId.toString()));
    }
}
