package com.utmn.chamortsev.hw9.controller;


import com.utmn.chamortsev.hw9.ThreadPoolDemoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/demo")
@CrossOrigin(origins = "*")
@Tag(name = "Thread Pool Demo API", description = "API для демонстрации работы с пулами потоков")
public class DemoController {

    private final ThreadPoolDemoService threadPoolDemoService;

    public DemoController(ThreadPoolDemoService threadPoolDemoService) {
        this.threadPoolDemoService = threadPoolDemoService;
    }

    @Operation(
            summary = "Запуск всех демонстраций",
            description = "Запускает демонстрации Future/cancel, invokeAll и ScheduledExecutorService"
    )
    @PostMapping("/run-all")
    public ResponseEntity<Map<String, Object>> runAllDemos() {
        try {
            Map<String, Object> results = threadPoolDemoService.runAllDemos();
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Ошибка выполнения демонстраций: " + e.getMessage()));
        }
    }

    @Operation(
            summary = "Демонстрация Future и cancel",
            description = "Запускает долгую задачу и пытается отменить её через 3 секунды"
    )
    @PostMapping("/future-cancel")
    public ResponseEntity<String> demonstrateFutureCancel() {
        try {
            String result = threadPoolDemoService.demonstrateFutureAndCancel();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Ошибка: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Демонстрация invokeAll",
            description = "Запускает несколько задач одновременно с помощью invokeAll()"
    )
    @PostMapping("/invoke-all")
    public ResponseEntity<?> demonstrateInvokeAll() {
        try {
            return ResponseEntity.ok(threadPoolDemoService.demonstrateInvokeAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Ошибка: " + e.getMessage()));
        }
    }
}