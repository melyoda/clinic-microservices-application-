package com.clinic.appointment_service.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "appointment.events";
    public static final String APPOINTMENT_SCHEDULED_ROUTING_KEY = "appointment.scheduled";
    public static final String APPOINTMENT_CANCELLED_ROUTING_KEY = "appointment.cancelled";
    public static final String APPOINTMENT_UPDATED_ROUTING_KEY = "appointment.updated";


    @Bean
    public TopicExchange appointmentEventsExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }
}