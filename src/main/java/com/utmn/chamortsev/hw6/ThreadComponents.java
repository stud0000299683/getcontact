package com.utmn.chamortsev.hw6;


import org.springframework.stereotype.Component;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class ThreadComponents {

    public volatile boolean stopFlag = false;

    public final AtomicInteger counter = new AtomicInteger(0);

    public final AtomicReference<String> cache = new AtomicReference<>(null);

    public String getFromCache() {
        String current = cache.get();
        if (current == null) {
            current = "Cached-Value-" + System.currentTimeMillis();
            cache.set(current);
        }
        return current;
    }
}
