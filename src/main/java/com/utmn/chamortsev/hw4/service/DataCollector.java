package com.utmn.chamortsev.hw4.service;


import com.utmn.chamortsev.hw4.model.Item;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component
public class DataCollector {
    private final List<Item> items = new ArrayList<>();
    private int processedCount = 0;
    private final Set<String> processedKeys = new HashSet<>();
    private final Object lock = new Object();
    private final ReentrantLock reentrantLock = new ReentrantLock();
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public synchronized void collectItem(Item item) {
        if (!isAlreadyProcessed(item.getKey())) {
            items.add(item);
            incrementProcessed();
            processedKeys.add(item.getKey());
            System.out.println(Thread.currentThread().getName() + " добавил: " + item.getKey());
        }
    }

    public void incrementProcessed() {
        synchronized (lock) {
            processedCount++;
            System.out.println(Thread.currentThread().getName() + " увеличил счетчик: " + processedCount);
        }
    }

    public boolean isAlreadyProcessed(String key) {
        readWriteLock.readLock().lock();
        try {
            return processedKeys.contains(key);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    public synchronized int getProcessedCount() {
        return processedCount;
    }

    public synchronized void printStatistics() {
        System.out.println("Обработано: " + processedCount + ", Уникальных: " + items.size());
    }
}
