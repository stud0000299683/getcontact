package com.utmn.chamortsev.hw9;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;

@Service
public class ThreadPoolDemoService {

    private static final Logger logger = LoggerFactory.getLogger(ThreadPoolDemoService.class);
    private final ScheduledExecutorService scheduledExecutor;
    private final ExecutorService fixedThreadPool;
    private final Random random = new Random();

    public ThreadPoolDemoService() {
        this.fixedThreadPool = Executors.newFixedThreadPool(3);
        this.scheduledExecutor = Executors.newScheduledThreadPool(2);
        startPeriodicTasks();
    }

    //  Future и cancel()
    public String demonstrateFutureAndCancel() {
        logger.info("=== Демонстрация Future и cancel() ===");

        ExecutorService executor = Executors.newSingleThreadExecutor();

        // Создаем долгую задачу
        Callable<String> longRunningTask = () -> {
            logger.info("Долгая задача началась");
            try {
                for (int i = 1; i <= 10; i++) {
                    Thread.sleep(1000); // Имитация работы - 1 секунда на шаг
                    logger.info("Долгая задача: шаг {}/10", i);

                    if (Thread.currentThread().isInterrupted()) {
                        logger.info("Задача прервана на шаге {}", i);
                        return "Задача прервана";
                    }
                }
                return "Долгая задача завершена успешно";
            } catch (InterruptedException e) {
                logger.info("Задача прервана через InterruptedException");
                return "Задача прервана";
            }
        };

        Future<String> future = executor.submit(longRunningTask);

        try {
            Thread.sleep(3000);

            // Пытаемся отменить задачу
            boolean cancelled = future.cancel(true);
            logger.info("Попытка отмены задачи: {}", cancelled ? "успешно" : "не удалось");

            if (!cancelled) {
                // Если не удалось отменить, пытаемся получить результат
                try {
                    String result = future.get(1, TimeUnit.SECONDS);
                    return "Результат задачи: " + result;
                } catch (TimeoutException e) {
                    return "Задача не завершилась в timeout";
                }
            } else {
                return "Задача успешно отменена";
            }

        } catch (Exception e) {
            return "Ошибка: " + e.getMessage();
        } finally {
            executor.shutdown();
        }
    }

    // invokeAll() с несколькими задачами
    public List<String> demonstrateInvokeAll() {
        logger.info("=== Демонстрация invokeAll() ===");

        // Создаем список задач
        List<Callable<String>> tasks = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            final int taskId = i;
            Callable<String> task = () -> {
                int sleepTime = 1000 + random.nextInt(2000); // 1-3 секунды
                logger.info("Задача {} началась, время выполнения: {} мс", taskId, sleepTime);

                Thread.sleep(sleepTime);

                boolean success = random.nextBoolean();
                String result = String.format("Задача %d завершена: %s", taskId, success ? "УСПЕХ" : "НЕУДАЧА");
                logger.info(result);

                return result;
            };
            tasks.add(task);
        }

        try {
            // Запускаем все задачи одновременно
            List<Future<String>> futures = fixedThreadPool.invokeAll(tasks);

            // Собираем результаты
            List<String> results = new ArrayList<>();
            for (Future<String> future : futures) {
                try {
                    String result = future.get();
                    results.add(result);
                } catch (ExecutionException e) {
                    results.add("Ошибка выполнения: " + e.getCause().getMessage());
                }
            }

            return results;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return List.of("Ошибка: поток прерван");
        }
    }

    // ScheduledExecutorService
    private void startPeriodicTasks() {
        // Задача для проверки статуса системы - каждые 10 секунд
        scheduledExecutor.scheduleAtFixedRate(() -> {
            try {
                logger.info("[Системный мониторинг] Проверка статуса системы...");
                // Имитация проверки
                Thread.sleep(500);
                logger.info("[Системный мониторинг] Статус: ОК, свободная память: {} MB",
                        Runtime.getRuntime().freeMemory() / (1024 * 1024));
            } catch (Exception e) {
                logger.error("[Системный мониторинг] Ошибка: {}", e.getMessage());
            }
        }, 0, 10, TimeUnit.SECONDS);

        // Задача для очистки "кэша" - каждые 30 секунд
        scheduledExecutor.scheduleAtFixedRate(() -> {
            try {
                logger.info("[Очистка кэша] Запуск процедуры очистки...");
                // Имитация очистки
                Thread.sleep(300);
                logger.info("[Очистка кэша] Очистка завершена успешно");
            } catch (Exception e) {
                logger.error("[Очистка кэша] Ошибка: {}", e.getMessage());
            }
        }, 5, 30, TimeUnit.SECONDS);

        // Мониторинг очереди - каждые 15 секунд
        scheduledExecutor.scheduleAtFixedRate(() -> {
            try {
                logger.info("[Мониторинг очереди] Проверка очереди задач...");
                ThreadPoolExecutor executor = (ThreadPoolExecutor) fixedThreadPool;
                logger.info("[Мониторинг очереди] Активные потоки: {}, Очередь: {}, Завершено: {}",
                        executor.getActiveCount(),
                        executor.getQueue().size(),
                        executor.getCompletedTaskCount());
            } catch (Exception e) {
                logger.error("[Мониторинг очереди] Ошибка: {}", e.getMessage());
            }
        }, 2, 15, TimeUnit.SECONDS);
    }

    // Метод для ручного запуска демонстраций
    public Map<String, Object> runAllDemos() {
        Map<String, Object> results = new HashMap<>();

        results.put("futureDemo", demonstrateFutureAndCancel());
        results.put("invokeAllDemo", demonstrateInvokeAll());
        results.put("scheduledTasks", "Периодические задачи запущены");
        results.put("timestamp", new Date());

        return results;
    }

    public void shutdown() {
        logger.info("Завершение работы ThreadPoolDemoService");
        fixedThreadPool.shutdown();
        scheduledExecutor.shutdown();

        try {
            if (!fixedThreadPool.awaitTermination(5, TimeUnit.SECONDS)) {
                fixedThreadPool.shutdownNow();
            }
            if (!scheduledExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduledExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            fixedThreadPool.shutdownNow();
            scheduledExecutor.shutdownNow();
        }
    }
}