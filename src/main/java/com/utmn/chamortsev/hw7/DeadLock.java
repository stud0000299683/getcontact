package com.utmn.chamortsev.hw7;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DeadLock {

    private final Object resourceA = new Object();
    private final Object resourceB = new Object();

    @PostConstruct
    public void demonstrateDeadlock() throws InterruptedException {
        System.out.println("Starting deadlock demonstration...");

        Thread thread1 = new Thread(() -> {
            synchronized (resourceA) {
                System.out.println("Thread 1: захватил resourceA");
                try { Thread.sleep(100); } catch (InterruptedException e) {}

                synchronized (resourceB) {
                    System.out.println("Thread 1: захватил resourceB");
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            synchronized (resourceB) {
                System.out.println("Thread 2: захватил resourceB");
                try { Thread.sleep(100); } catch (InterruptedException e) {}

                synchronized (resourceA) {
                    System.out.println("Thread 2: захватил resourceA");
                }
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
    }
}
