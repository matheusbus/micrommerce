package com.esw.orderservice.kafka;

import com.esw.orderservice.dto.OrderCancelledEvent;
import com.esw.orderservice.dto.OrderCreatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaOrderProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaOrderProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishOrderCreated(OrderCreatedEvent event) {
        kafkaTemplate.send("order-created", event);
    }

    public void publishOrderCancelled(OrderCancelledEvent event) {
        kafkaTemplate.send("order-cancelled", event);
    }
}
