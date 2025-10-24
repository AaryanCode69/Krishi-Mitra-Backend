package com.example.krishimitrabackend.configs;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    public static final String Queue_Name = "Image_Processing_Queue";
    public static final String Exchange_Name = "Image_Processing_Exchange";
    public static final String RoutingKey_Name = "Image_Processing_RoutingKey";

    @Bean
    public Queue queue() {
        return new Queue(Queue_Name, true);
    }

    @Bean
    public Exchange exchange() {
        return new TopicExchange(Exchange_Name);
    }

    @Bean
    public Binding binding(Queue queue, Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(RoutingKey_Name).noargs();
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
