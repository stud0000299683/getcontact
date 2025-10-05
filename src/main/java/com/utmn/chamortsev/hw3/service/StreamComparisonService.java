package com.utmn.chamortsev.hw3.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class StreamComparisonService {

    private List<Integer> numbers;
    private static final int SIZE = 1_000_000_0;

    @PostConstruct
    public void compareStreams() {
        generateNumbers();

        System.out.println("=== Сравнение stream() vs parallelStream() ===");
        System.out.println("Размер списка: " + SIZE + " чисел\n");

        // Обычный stream
        long sequentialTime = measureSequentialStream();

        // Parallel stream
        long parallelTime = measureParallelStream();

        // Вывод результатов
        printResults(sequentialTime, parallelTime);
    }

    private void generateNumbers() {
        Random random = new Random();
        numbers = random.ints(SIZE, 1, 1000)
                .boxed()
                .collect(Collectors.toList());
        System.out.println("Список из " + SIZE + " чисел создан");
    }

    private long measureSequentialStream() {
        System.out.println("\n--- Обычный stream() ---");

        long startTime = System.currentTimeMillis();

        long result = numbers.stream()
                .filter(n -> n % 2 == 0)      // Фильтрация: только четные
                .map(n -> n * 2)              // Преобразование: умножить на 2
                .mapToLong(Long::valueOf)
                .sum();                       // Агрегация: сумма

        long endTime = System.currentTimeMillis();

        System.out.println("Результат: " + result);
        System.out.println("Время выполнения: " + (endTime - startTime) + " мс");

        return endTime - startTime;
    }

    private long measureParallelStream() {
        System.out.println("\n--- Parallel stream() ---");

        long startTime = System.currentTimeMillis();

        long result = numbers.parallelStream()
                .filter(n -> n % 2 == 0)      // Фильтрация: только четные
                .map(n -> n * 2)              // Преобразование: умножить на 2
                .mapToLong(Long::valueOf)
                .sum();                       // Агрегация: сумма

        long endTime = System.currentTimeMillis();

        System.out.println("Результат: " + result);
        System.out.println("Время выполнения: " + (endTime - startTime) + " мс");

        return endTime - startTime;
    }

    private void printResults(long sequentialTime, long parallelTime) {
        System.out.println("\n=== РЕЗУЛЬТАТЫ ===");
        System.out.println("Обычный stream: " + sequentialTime + " мс");
        System.out.println("Parallel stream: " + parallelTime + " мс");

        double speedup = (double) sequentialTime / parallelTime;
        System.out.printf("Ускорение: %.2f раз\n", speedup);

        if (speedup > 1) {
            System.out.println("Parallel stream БЫСТРЕЕ");
        } else {
            System.out.println("Обычный stream БЫСТРЕЕ");
        }
    }
}