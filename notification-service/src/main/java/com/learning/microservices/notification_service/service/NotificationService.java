package com.learning.microservices.notification_service.service;

import com.learning.microservices.notification_service.model.InventoryEvent;
import com.learning.microservices.notification_service.model.Notification;
import com.learning.microservices.notification_service.model.OrderEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class NotificationService {
    private final List<Notification> notifications = new CopyOnWriteArrayList<>();

    @RabbitListener(queues = "order.created")
    public void handleOrderCreated(OrderEvent event) {
        String message = String.format(
            "New order created! Order ID: %s, Customer: %s, Product: %s, Quantity: %d, Total: $%.2f",
            event.getOrderId(),
            event.getCustomerId(),
            event.getProductId(),
            event.getQuantity(),
            event.getTotalPrice()
        );

        Notification notification = new Notification(
            java.util.UUID.randomUUID().toString(),
            "ORDER_CREATED",
            message,
            LocalDateTime.now()
        );

        notifications.add(notification);
        System.out.println("📧 EMAIL NOTIFICATION: " + message);
    }

    @RabbitListener(queues = "inventory.updated")
    public void handleInventoryUpdated(InventoryEvent event) {
        String message;
        if ("RESERVED".equals(event.getStatus())) {
            message = String.format(
                "Inventory reserved! Order ID: %s, Product: %s, Quantity: %d, Remaining Stock: %d",
                event.getOrderId(),
                event.getProductId(),
                event.getQuantity(),
                event.getCurrentStock()
            );
        } else {
            message = String.format(
                "⚠️ Insufficient stock! Order ID: %s, Product: %s, Requested: %d, Available: %d",
                event.getOrderId(),
                event.getProductId(),
                event.getQuantity(),
                event.getCurrentStock()
            );
        }

        Notification notification = new Notification(
            java.util.UUID.randomUUID().toString(),
            "INVENTORY_UPDATED",
            message,
            LocalDateTime.now()
        );

        notifications.add(notification);
        System.out.println("📧 EMAIL NOTIFICATION: " + message);
    }

    public List<Notification> getAllNotifications() {
        return new ArrayList<>(notifications);
    }
}