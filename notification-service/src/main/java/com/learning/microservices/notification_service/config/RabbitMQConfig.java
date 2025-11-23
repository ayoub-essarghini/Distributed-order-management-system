package com.learning.microservices.notification_service.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    
    public static final String ORDER_QUEUE = "order.created";
    public static final String INVENTORY_QUEUE = "inventory.updated";
    
    @Bean
    public Queue orderQueue() {
        return new Queue(ORDER_QUEUE, false);
    }
    
    @Bean
    public Queue inventoryQueue() {
        return new Queue(INVENTORY_QUEUE, false);
    }
    
    @Bean
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }
}