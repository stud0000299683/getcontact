package com.utmn.chamortsev.hw6;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.concurrent.CountDownLatch;

@Component
public class ThreadRunner implements CommandLineRunner {

    private final ThreadComponents components;

    public ThreadRunner(ThreadComponents components) {
        this.components = components;
    }

    @Override
    public void run(String... args) throws InterruptedException {
        System.out.println("=== Нагрузочное тестирование ===");

        int threadCount = 5;
        CountDownLatch latch = new CountDownLatch(threadCount);

        // Создаем и запускаем потоки
        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            new Thread(() -> {
                try {
                    work(threadId);
                    latch.countDown();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, "Thread-" + i).start();
        }

        Thread.sleep(3000);

        // Останавливаем потоки
        components.stopFlag = true;
        System.out.println("Stop flag установлен");

        // Ждем завершения всех потоков
        latch.await();

        // Выводим итог
        System.out.println("\n=== Результаты ===");
        System.out.println("Финальное значение счетчика: " + components.counter.get());
        System.out.println("Значение кэша: " + components.cache.get());
    }

    private void work(int threadId) throws InterruptedException {
        while (!components.stopFlag) {
            // Инкремент счетчика
            int newValue = components.counter.incrementAndGet();

            // Работа с кэшем
            String cached = components.getFromCache();

            // Вывод информации
            System.out.printf("Thread-%d: counter=%d, cache=%s%n",
                    threadId, newValue, cached);

            Thread.sleep(100);
        }
        System.out.println("Thread-" + threadId + " завершен");
    }
}