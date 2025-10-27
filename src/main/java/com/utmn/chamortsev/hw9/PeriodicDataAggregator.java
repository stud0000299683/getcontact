package com.utmn.chamortsev.hw9;

import com.utmn.chamortsev.hw9.entity.UrlEntity;
import com.utmn.chamortsev.hw9.repository.UrlRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class PeriodicDataAggregator {

    private static final Logger logger = LoggerFactory.getLogger(PeriodicDataAggregator.class);

    private final UrlRepository urlRepository;
    private final ScheduledExecutorService scheduler;
    private final ExecutorService workerPool;

    private static final int AGGREGATION_INTERVAL_SECONDS = 30;
    private static final int MAX_CONCURRENT_REQUESTS = 3;

    public PeriodicDataAggregator(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.workerPool = Executors.newFixedThreadPool(MAX_CONCURRENT_REQUESTS);
    }

    @PostConstruct
    public void startAggregation() {
        logger.info("Запуск PeriodicDataAggregator с интервалом {} секунд", AGGREGATION_INTERVAL_SECONDS);

        scheduler.scheduleWithFixedDelay(() -> {
            try {
                performAggregationCycle();
            } catch (Exception e) {
                logger.error("Критическая ошибка в цикле агрегации: {}", e.getMessage(), e);
            }
        }, 10, AGGREGATION_INTERVAL_SECONDS, TimeUnit.SECONDS);
    }

    @PreDestroy
    public void shutdown() {
        logger.info("Остановка PeriodicDataAggregator");
        scheduler.shutdown();
        workerPool.shutdown();

        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
            if (!workerPool.awaitTermination(5, TimeUnit.SECONDS)) {
                workerPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            scheduler.shutdownNow();
            workerPool.shutdownNow();
        }
    }

    private void performAggregationCycle() {
        logger.info("=== Начало цикла агрегации данных ===");

        // Получаем список активных URL
        List<Long> entityIds = getActiveEntityIds();

        if (entityIds.isEmpty()) {
            logger.info("Нет активных сущностей для агрегации");
            return;
        }

        logger.info("Найдено {} активных сущностей для обработки", entityIds.size());

        // Создаем задачи для каждого URL
        List<Callable<AggregationResult>> tasks = entityIds.stream()
                .map(this::createAggregationTask)
                .collect(Collectors.toList());

        // Запускаем все задачи асинхронно
        try {
            List<Future<AggregationResult>> futures = workerPool.invokeAll(tasks);

            // Обрабатываем результаты
            processAggregationResults(futures);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Агрегация прервана");
        }
    }

    private List<Long> getActiveEntityIds() {
        return urlRepository.findByActiveTrueOrderByCreatedAtDesc()
                .stream()
                .map(UrlEntity::getId)
                .collect(Collectors.toList());
    }

    private Callable<AggregationResult> createAggregationTask(Long entityId) {
        return () -> {
            logger.debug("Начало агрегации данных для сущности {}", entityId);

            try {
                // Имитация запроса к внешнему источнику
                ExternalData externalData = fetchExternalData(entityId);

                // Обработка данных
                AggregationResult result = processExternalData(entityId, externalData);

                logger.debug("Успешная агрегация для сущности {}: {}", entityId, result.getStatus());
                return result;

            } catch (Exception e) {
                logger.error("Ошибка агрегации для сущности {}: {}", entityId, e.getMessage());
                return new AggregationResult(entityId, AggregationStatus.FAILED,
                        "Ошибка: " + e.getMessage(), null);
            }
        };
    }

    // Имитация запроса к внешнему источнику (может выбрасывать исключения)
    private ExternalData fetchExternalData(Long entityId) throws DataFetchException {
        Random random = new Random();

        // Имитация сетевой задержки
        try {
            Thread.sleep(500 + random.nextInt(1500)); // 0.5-2 секунды
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new DataFetchException("Запрос прерван");
        }

        // Имитация случайных ошибок (10% вероятность)
        if (random.nextDouble() < 0.1) {
            throw new DataFetchException("Внешний источник недоступен");
        }

        // Имитация успешного ответа
        return new ExternalData(
                entityId,
                "Статус: онлайн",
                System.currentTimeMillis(),
                Map.of(
                        "requests", random.nextInt(1000),
                        "responseTime", 50 + random.nextInt(200),
                        "uptime", 95 + random.nextInt(5)
                )
        );
    }

    private AggregationResult processExternalData(Long entityId, ExternalData externalData) {
        // Здесь может быть сложная логика обработки
        String statusMessage = String.format("Данные обновлены: %s, метрики: %s",
                externalData.getStatus(), externalData.getMetrics());

        // Сохраняем или логируем результат
        logger.info("Обработаны данные для сущности {}: {}", entityId, statusMessage);

        return new AggregationResult(entityId, AggregationStatus.SUCCESS, statusMessage, externalData);
    }

    private void processAggregationResults(List<Future<AggregationResult>> futures) {
        int successCount = 0;
        int failedCount = 0;

        for (Future<AggregationResult> future : futures) {
            try {
                AggregationResult result = future.get(5, TimeUnit.SECONDS);

                if (result.getStatus() == AggregationStatus.SUCCESS) {
                    successCount++;
                    // Здесь можно сохранить результат в БД или отправить дальше
                    saveAggregationResult(result);
                } else {
                    failedCount++;
                }

            } catch (TimeoutException e) {
                logger.error("Таймаут получения результата агрегации");
                failedCount++;
            } catch (Exception e) {
                logger.error("Ошибка получения результата агрегации: {}", e.getMessage());
                failedCount++;
            }
        }

        logger.info("=== Цикл агрегации завершен. Успешно: {}, Неудачно: {} ===",
                successCount, failedCount);
    }

    private void saveAggregationResult(AggregationResult result) {
        // Имитация сохранения результата
        logger.debug("Сохранение результата для сущности {}: {}",
                result.getEntityId(), result.getMessage());
    }

    // Вспомогательные классы
    public static class ExternalData {
        private final Long entityId;
        private final String status;
        private final long timestamp;
        private final Map<String, Object> metrics;

        public ExternalData(Long entityId, String status, long timestamp, Map<String, Object> metrics) {
            this.entityId = entityId;
            this.status = status;
            this.timestamp = timestamp;
            this.metrics = metrics;
        }

        // Getters
        public Long getEntityId() { return entityId; }
        public String getStatus() { return status; }
        public long getTimestamp() { return timestamp; }
        public Map<String, Object> getMetrics() { return metrics; }
    }

    public static class AggregationResult {
        private final Long entityId;
        private final AggregationStatus status;
        private final String message;
        private final ExternalData data;

        public AggregationResult(Long entityId, AggregationStatus status, String message, ExternalData data) {
            this.entityId = entityId;
            this.status = status;
            this.message = message;
            this.data = data;
        }

        // Getters
        public Long getEntityId() { return entityId; }
        public AggregationStatus getStatus() { return status; }
        public String getMessage() { return message; }
        public ExternalData getData() { return data; }
    }

    public enum AggregationStatus {
        SUCCESS, FAILED
    }

    public static class DataFetchException extends Exception {
        public DataFetchException(String message) {
            super(message);
        }
    }
}
