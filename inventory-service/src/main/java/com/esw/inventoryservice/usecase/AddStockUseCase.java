package com.esw.inventoryservice.usecase;

import com.esw.inventoryservice.dto.AddStockRequest;
import com.esw.inventoryservice.model.Inventory;
import com.esw.inventoryservice.repository.InventoryRepository;
import org.springframework.stereotype.Service;

@Service
public class AddStockUseCase {

    private final InventoryRepository repository;

    public AddStockUseCase(InventoryRepository repository) {
        this.repository = repository;
    }

    public Inventory handle(AddStockRequest request) {
        Inventory inv = repository.findByProductId(request.productId())
                .orElse(new Inventory(request.productId(), 0, 0));

        inv.setAvailable(inv.getAvailable() + request.quantity());

        return repository.save(inv);
    }
}