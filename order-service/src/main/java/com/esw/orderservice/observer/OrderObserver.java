package com.esw.orderservice.observer;

import com.esw.orderservice.model.Order;
import com.esw.orderservice.model.OrderStatus;

public interface OrderObserver {
    void onOrderStatusChanged(Order order, OrderStatus oldStatus, OrderStatus newStatus);
}
