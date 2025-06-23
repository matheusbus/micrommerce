package com.esw.inventoryservice.usecase;

import com.esw.inventoryservice.dto.ReserveStockRequest;
import com.esw.inventoryservice.dto.StockReservedEvent;
import com.esw.inventoryservice.exception.NotFoundException;
import com.esw.inventoryservice.kafka.KafkaStockProducer;
import com.esw.inventoryservice.model.Inventory;
import com.esw.inventoryservice.repository.InventoryRepository;
import org.springframework.stereotype.Service;

@Service
public class ReserveStockUseCase {

    private final InventoryRepository repository;
    private final KafkaStockProducer kafkaProducer;

    public ReserveStockUseCase(InventoryRepository repository, KafkaStockProducer kafkaProducer) {
        this.repository = repository;
        this.kafkaProducer = kafkaProducer;
    }

    public Inventory handle(ReserveStockRequest request) {
        Inventory inv = repository.findByProductId(request.productId())
                .orElseThrow(() -> new NotFoundException(Inventory.class, "productId", request.productId().toString()));

        boolean success = inv.reserve(request.quantity());

        if (success) {
            kafkaProducer.sendStockReserved(new StockReservedEvent(request.orderId(), true));
        } else {
            kafkaProducer.sendStockReserved(new StockReservedEvent(request.orderId(), false));
        }

        return repository.save(inv);
    }
}
