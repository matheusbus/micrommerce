package com.esw.inventoryservice.kafka;

import com.esw.inventoryservice.dto.StockReleasedEvent;
import com.esw.inventoryservice.dto.StockReservedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaStockProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaStockProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendStockReserved(StockReservedEvent event) {
        kafkaTemplate.send("stock-reserved", event);
    }

    public void sendStockReleased(StockReleasedEvent event) {
        kafkaTemplate.send("stock-released", event);
    }
}