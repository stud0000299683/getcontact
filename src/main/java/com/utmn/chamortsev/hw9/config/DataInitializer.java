package com.utmn.chamortsev.hw9.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Override
    public void run(String... args) throws Exception {
        logger.info("=== URL Parser Начало работы ===");
        logger.info("H2 Console: http://localhost:8080/h2-console");
        logger.info("JDBC URL: jdbc:h2:mem:urlparserdb");
        logger.info("Username: sa, Password: (empty)");
        logger.info("");
        logger.info("=== API Endpoints ===");
        logger.info("GET  /api/urls              - Список всех URLs");
        logger.info("POST /api/urls              - Добавить новый URL");
        logger.info("POST /api/urls/batch        - Добавить несколько URLs");
        logger.info("POST /api/urls/process      - Обработать все URLs");
        logger.info("GET  /api/urls/results      - Вывести результат");
        logger.info("GET  /api/urls/statistics   - Статистика");
        logger.info("GET  /api/urls/thread-pool-info - Информация по потокам");
        logger.info("PUT  /api/urls/{id}         - Обновить URL");
        logger.info("DELETE /api/urls/{id}       - Удалить URL");
        logger.info("");
        logger.info("=== Demo Endpoints ===");
        logger.info("POST /api/demo/run-all      - Запуск всех демонстраций");
        logger.info("POST /api/demo/future-cancel - Демо Future и cancel");
        logger.info("POST /api/demo/invoke-all   - Демо invokeAll");
        logger.info("");
        logger.info("=== Сервисы ===");
        logger.info("PeriodicDataAggregator      - Запущен, интервал 30 сек");
        logger.info("ThreadPoolDemoService       - Запущен с периодическими задачами");
        logger.info("");
        logger.info("Первоначальная загрузка завершена");
    }
}