package com.esw.inventoryservice.kafka;

import com.esw.inventoryservice.dto.*;
import com.esw.inventoryservice.exception.NotFoundException;
import com.esw.inventoryservice.model.Inventory;
import com.esw.inventoryservice.repository.InventoryRepository;
import com.esw.inventoryservice.usecase.ReserveStockUseCase;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaStockConsumer {

    private final InventoryRepository repository;
    private final ReserveStockUseCase reserveStockUseCase;

    public KafkaStockConsumer(InventoryRepository repository, ReserveStockUseCase reserveStockUseCase) {
        this.repository = repository;
        this.reserveStockUseCase = reserveStockUseCase;
    }

    @KafkaListener(topics = "order-created", groupId = "inventory-group")
    public void handleOrderCreated(OrderCreatedEvent event) {
        for (OrderItemDTO itemDTO: event.items()) {
            ReserveStockRequest request = new ReserveStockRequest(
                    itemDTO.productId(),
                    itemDTO.quantity(),
                    event.orderId()
            );

            reserveStockUseCase.handle(request);
        }
    }

    @KafkaListener(topics = "payment-failed", groupId = "inventory-group")
    public void handlePaymentFailed(PaymentFailedEvent event) {
        Inventory inv = repository.findByProductId(event.productId())
                .orElseThrow(() -> new NotFoundException(Inventory.class, "productId", event.productId().toString()));

        inv.release(event.quantity());

        repository.save(inv);
    }

    @KafkaListener(topics = "payment-completed", groupId = "inventory-group")
    public void handlePaymentCompleted(PaymentCompletedEvent event) {
        Inventory inv = repository.findByProductId(event.productId())
                .orElseThrow(() -> new NotFoundException(Inventory.class, "productId", event.productId().toString()));

        inv.confirmReservation(event.quantity());
        repository.save(inv);
    }
}
