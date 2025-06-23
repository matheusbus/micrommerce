package com.esw.orderservice.controller;

import com.esw.orderservice.dto.CreateOrderRequest;
import com.esw.orderservice.dto.OrderResponse;
import com.esw.orderservice.model.Order;
import com.esw.orderservice.model.OrderStatus;
import com.esw.orderservice.usecase.CancelOrderUseCase;
import com.esw.orderservice.usecase.CreateOrderUseCase;
import com.esw.orderservice.usecase.GetOrderByIdUseCase;
import com.esw.orderservice.usecase.ListOrdersUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/order")
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final GetOrderByIdUseCase getOrderByIdUseCase;
    private final ListOrdersUseCase listOrdersUseCase;
    private final CancelOrderUseCase cancelOrderUseCase;

    public OrderController(CreateOrderUseCase createOrderUseCase, GetOrderByIdUseCase getOrderByIdUseCase, ListOrdersUseCase listOrdersUseCase, CancelOrderUseCase cancelOrderUseCase) {
        this.createOrderUseCase = createOrderUseCase;
        this.getOrderByIdUseCase = getOrderByIdUseCase;
        this.listOrdersUseCase = listOrdersUseCase;
        this.cancelOrderUseCase = cancelOrderUseCase;
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(getOrderByIdUseCase.handle(id));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> list(@RequestParam Long userId, @RequestParam String status) {
        return ResponseEntity.ok(listOrdersUseCase.handle(userId, status));
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody @Valid CreateOrderRequest request) {
        Order order = createOrderUseCase.handle(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<String> cancel(@PathVariable String id) {
        cancelOrderUseCase.handle(id);
        return ResponseEntity.ok("Order cancelled successfully");
    }
}
