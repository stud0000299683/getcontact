package com.utmn.chamortsev.hw12;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// Обработчики
@Component
public class NotificationHandlers {

    @Autowired
    private EventService eventService;

    @PostConstruct
    public void init() {
        eventService.registerHandler("USER_LOGIN", this::sendWelcome);
        eventService.registerHandler("ORDER_CREATED", this::sendOrderConfirm);
    }

    private void sendWelcome(Event event) {
        System.out.println("Welcome email: " + event.getData());
    }

    private void sendOrderConfirm(Event event) {
        System.out.println("Order confirmation: " + event.getData());
    }
}
