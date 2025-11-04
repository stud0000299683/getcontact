package com.utmn.chamortsev.hw12;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

// Сервис событий
@Service
@Async
public class EventService {

    private final Map<String, List<Consumer<Event>>> handlers = new ConcurrentHashMap<>();
    private final Queue<Event> eventQueue = new ConcurrentLinkedQueue<>();

    @EventListener
    public void handleEvent(Event event) {
        eventQueue.offer(event);
        processEvent(event);
    }

    private void processEvent(Event event) {
        handlers.getOrDefault(event.getType(), List.of())
                .forEach(handler -> handler.accept(event));
    }

    public void registerHandler(String eventType, Consumer<Event> handler) {
        handlers.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>())
                .add(handler);
    }

    public void publish(String type, Object data) {
        ApplicationEventPublisher publisher = null;
        publisher.publishEvent(new Event(type, data));
    }
}
