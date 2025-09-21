package com.utmn.chamortsev.hw2.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class ThreadStateMonitor {

    private final Lock lock = new ReentrantLock();
    private final CountDownLatch latch = new CountDownLatch(1);
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private Thread thread1;
    private Thread thread2;
    private Thread thread3;
    private Thread blockedDemoThread;

    public void startDemo() {
        System.out.println("=== Демонстрация состояний потоков ===");

        // Создаем потоки в состоянии NEW
        thread1 = new Thread(this::runnableThreadTask, "Runnable-Thread");
        thread2 = new Thread(this::waitingThreadTask, "Waiting-Thread");
        thread3 = new Thread(this::blockedThreadTask, "Blocked-Thread");

        startMonitoring();
        thread1.start();
        thread2.start();
        thread3.start();
    }

    @Async
    public void startMonitoring() {
        scheduler.scheduleAtFixedRate(() -> {
            printThreadStates();
        }, 0, 500, TimeUnit.MILLISECONDS);
    }

    private void printThreadStates() {
        System.out.println("\n--- Текущие состояния потоков ---");
        if (thread1 != null) System.out.println(thread1.getName() + ": " + thread1.getState());
        if (thread2 != null) System.out.println(thread2.getName() + ": " + thread2.getState());
        if (thread3 != null) System.out.println(thread3.getName() + ": " + thread3.getState());
        if (blockedDemoThread != null) System.out.println(blockedDemoThread.getName() + ": " + blockedDemoThread.getState());
        System.out.println("--------------------------------");
    }

    private void runnableThreadTask() {
        System.out.println(Thread.currentThread().getName() + ": NEW -> RUNNABLE");

        for (int i = 0; i < 3; i++) {
            System.out.println(Thread.currentThread().getName() + " работает: " + i);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
        System.out.println(Thread.currentThread().getName() + ": завершен");
    }

    private void waitingThreadTask() {
        System.out.println(Thread.currentThread().getName() + ": NEW -> RUNNABLE");

        try {
            System.out.println(Thread.currentThread().getName() + ": переходит в TIMED_WAITING");
            Thread.sleep(1500);

            System.out.println(Thread.currentThread().getName() + ": переходит в WAITING");
            synchronized (this) {
                latch.await();
            }

            System.out.println(Thread.currentThread().getName() + ": продолжает работу после WAITING");
            Thread.sleep(500);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
        System.out.println(Thread.currentThread().getName() + ": завершен");
    }

    private void blockedThreadTask() {
        System.out.println(Thread.currentThread().getName() + ": NEW -> RUNNABLE");

        try {
            Thread.sleep(200);

            System.out.println(Thread.currentThread().getName() + ": пытается захватить lock");

            if (lock.tryLock(100, TimeUnit.MILLISECONDS)) {
                try {
                    System.out.println(Thread.currentThread().getName() + ": захватил lock");
                    Thread.sleep(1000);
                } finally {
                    lock.unlock();
                    System.out.println(Thread.currentThread().getName() + ": освободил lock");
                }
            } else {
                System.out.println(Thread.currentThread().getName() + ": не смог захватить lock");
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
        System.out.println(Thread.currentThread().getName() + ": завершен");
    }

    public void demonstrateBlockedState() {
        System.out.println("\n=== Демонстрация BLOCKED состояния ===");
        Thread lockHolder = new Thread(() -> {
            System.out.println("Lock-Holder: захватывает lock");
            lock.lock();
            try {
                System.out.println("Lock-Holder: удерживает lock 3 секунды");
                Thread.sleep(3000);
                System.out.println("Lock-Holder: освобождает lock");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        }, "Lock-Holder");

        blockedDemoThread = new Thread(() -> {
            System.out.println("Blocked-Thread: запущен");

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            System.out.println("Blocked-Thread: пытается захватить lock (должен быть BLOCKED)");
            lock.lock();
            try {
                System.out.println("Blocked-Thread: захватил lock");
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
                System.out.println("Blocked-Thread: освободил lock");
            }

            System.out.println("Blocked-Thread: завершен");
        }, "Blocked-Demo-Thread");

        lockHolder.start();
        blockedDemoThread.start();

        monitorBlockedDemo(lockHolder, blockedDemoThread);
    }

    @Async
    private void monitorBlockedDemo(Thread lockHolder, Thread blockedThread) {
        ScheduledExecutorService demoMonitor = Executors.newScheduledThreadPool(1);
        demoMonitor.scheduleAtFixedRate(() -> {
            System.out.println("\n--- BLOCKED Demo States ---");
            System.out.println(lockHolder.getName() + ": " + lockHolder.getState());
            System.out.println(blockedThread.getName() + ": " + blockedThread.getState());
            System.out.println("---------------------------");
        }, 0, 300, TimeUnit.MILLISECONDS);

        demoMonitor.schedule(() -> {
            demoMonitor.shutdown();
        }, 5, TimeUnit.SECONDS);
    }
}
