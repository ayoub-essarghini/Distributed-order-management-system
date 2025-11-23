package com.learning.microservices.order_service.service;

import com.learning.microservices.order_service.dto.OrderRequest;
import com.learning.microservices.order_service.model.Order;

import java.util.Collection;

public interface OrderServiceImpl {
    public Order createOrder(OrderRequest orderRequest);
    public Order getOrder(String orderId);
    public Collection<Order> getOrders();
    public void updateOrderStatus(String orderId, String status);
}
