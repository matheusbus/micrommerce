package com.esw.orderservice.observer;

import com.esw.orderservice.model.Order;
import com.esw.orderservice.model.OrderStatus;
import org.springframework.stereotype.Component;

@Component
public class NotificationOrderObserver implements OrderObserver {

    @Override
    public void onOrderStatusChanged(Order order, OrderStatus oldStatus, OrderStatus newStatus) {
        // Simula envio de notificação
        // Implementar envio de mensagem kafka para notification-service
        System.out.println("🔔 Your order status has changed: " + oldStatus + " → " + newStatus);
    }
}
