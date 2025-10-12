package com.utmn.chamortsev.hw4.controller;


import com.utmn.chamortsev.hw4.model.Item;
import com.utmn.chamortsev.hw4.service.DataCollector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ConcurrentController {

    @Autowired
    private DataCollector dataCollector;

    @GetMapping("/add")
    public String addItem(@RequestParam String key, @RequestParam String data) {
        Item item = new Item(key, data);
        dataCollector.collectItem(item);
        return "Item добавлен: " + key + ", данные: " + data;
    }

    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("processedCount", dataCollector.getProcessedCount());
        stats.put("status", "OK");
        return stats;
    }

    @PostMapping("/add")
    public String addItemPost(@RequestParam String key, @RequestParam String data) {
        Item item = new Item(key, data);
        dataCollector.collectItem(item);
        return "Item добавлен через POST: " + key + ", данные: " + data;
    }
}
