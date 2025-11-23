package com.learning.microservices.order_service.service;

import com.learning.microservices.order_service.config.RabbitMQConfig;
import com.learning.microservices.order_service.dto.OrderRequest;
import com.learning.microservices.order_service.model.Order;
import com.learning.microservices.order_service.model.OrderEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class OrderService implements OrderServiceImpl{

    private final RabbitTemplate rabbitTemplate;
    private final ConcurrentHashMap<String, Order> orderStore = new ConcurrentHashMap<>();

    @Override
    public Order createOrder(OrderRequest orderRequest) {
        String orderId = UUID.randomUUID().toString();

        double totalPrice =  orderRequest.getPrice() * orderRequest.getQuantity();

        Order order = new Order(orderId,
                orderRequest.getCustomerId(),
                orderRequest.getProductId(),
                orderRequest.getQuantity(),
                totalPrice,
                "PENDING", LocalDateTime.now()
                );
        orderStore.put(orderId, order);

        OrderEvent orderEvent = new OrderEvent(orderId,
                orderRequest.getCustomerId(),
                orderRequest.getProductId(),
                orderRequest.getQuantity(),
                totalPrice,
                "ORDER_CREATED", LocalDateTime.now()
        );

        rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_QUEUE, orderEvent);
        System.out.println("Order created and event published: " + orderId);

    return order;

    }

    @Override
    public Order getOrder(String orderId) {
        return orderStore.get(orderId);
    }

    @Override
    public Collection<Order> getOrders() {
        return orderStore.values();
    }

    @Override
    public void updateOrderStatus(String orderId, String status) {
        if (orderStore.containsKey(orderId)) {
            Order order = orderStore.get(orderId);
            order.setStatus(status);
            orderStore.put(orderId, order);
        }

    }
}
