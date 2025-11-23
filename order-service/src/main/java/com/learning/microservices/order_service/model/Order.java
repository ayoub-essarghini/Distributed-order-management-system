package com.learning.microservices.order_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private String orderId;
    private String customerId;
    private String productId;
    private int quantity;
    private double totalPrice;
    private String status;
    private LocalDateTime createdAt;
}