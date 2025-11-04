package com.utmn.chamortsev.hw12;

import org.springframework.context.ApplicationEvent;

import java.util.UUID;

// Простое событие
public class Event extends ApplicationEvent {
    private String id;
    private String type;
    private Object data;

    public Event(String type, Object data) {
        super(data);
        this.id = UUID.randomUUID().toString();
        this.type = type;
        this.data = data;
    }

    // Геттеры
    public String getId() { return id; }
    public String getType() { return type; }
    public Object getData() { return data; }
}
