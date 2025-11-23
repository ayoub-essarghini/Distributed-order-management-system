package com.learning.microservices.order_service.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

  public static final String ORDER_QUEUE = "order.created";

  @Bean
  public Queue orderQueue() {
    return new Queue(ORDER_QUEUE);
  }

  @Bean
  public MessageConverter messageConverter() {
    return new JacksonJsonMessageConverter();
  }


}