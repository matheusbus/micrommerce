package com.esw.inventoryservice.usecase;

import com.esw.inventoryservice.exception.NotFoundException;
import com.esw.inventoryservice.model.Inventory;
import com.esw.inventoryservice.repository.InventoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllInventoryUseCase {

    private final InventoryRepository repository;

    public GetAllInventoryUseCase(InventoryRepository repository) {
        this.repository = repository;
    }

    public List<Inventory> handle() {
        return repository.findAll();
    }
}
