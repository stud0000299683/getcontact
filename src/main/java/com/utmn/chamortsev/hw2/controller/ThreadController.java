package com.utmn.chamortsev.hw2.controller;

import com.utmn.chamortsev.hw2.service.ThreadStateMonitor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ThreadController {

    private final ThreadStateMonitor monitor;

    public ThreadController(ThreadStateMonitor monitor) {
        this.monitor = monitor;
    }

    @GetMapping("/start")
    public String startDemo() {
        monitor.startDemo();
        return "Основная демонстрация запущена! Смотрите консоль.";
    }

    @GetMapping("/blocked")
    public String demonstrateBlocked() {
        monitor.demonstrateBlockedState();
        return "Демонстрация BLOCKED состояния запущена! Смотрите консоль.";
    }

    @GetMapping("/all")
    public String demonstrateAll() {
        monitor.startDemo();

        // Запускаем blocked demo через секунду
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                monitor.demonstrateBlockedState();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();

        return "Все демонстрации запущены! Смотрите консоль.";
    }
}