package com.utmn.chamortsev.hw1.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class ResultService {

    public CompletableFuture<String> asyncCalculation(int number) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Имитация долгих вычислений
                Thread.sleep(2000);
                int result = number * number;
                return "Результат вычисления: " + result;
            } catch (InterruptedException e) {
                throw new RuntimeException("Вычисления прерваны", e);
            }
        });
    }

    public CompletableFuture<String> asyncProcess(String data) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1500);
                return "Обработанные данные: " + data.toUpperCase();
            } catch (InterruptedException e) {
                throw new RuntimeException("Обработка прервана", e);
            }
        });
    }
}