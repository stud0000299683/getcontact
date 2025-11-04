package com.utmn.chamortsev.hw12;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

// Кэш сервис
@Service
@CacheConfig(cacheNames = "events")
public class CacheService {

    @Cacheable(key = "#id")
    public Event getEvent(String id) {
        return null; // Будет вызвано только если нет в кэше
    }

    @CachePut(key = "#event.id")
    public Event cacheEvent(Event event) {
        return event;
    }

    @CacheEvict(key = "#id")
    public void evictEvent(String id) {
    }
}
