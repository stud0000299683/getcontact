package com.utmn.chamortsev.hw4.service;


import com.utmn.chamortsev.hw4.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.util.Random;

@Service
public class ConcurrentAccessService {


    private DataCollector dataCollector;

    public ConcurrentAccessService(DataCollector dataCollector) {
        this.dataCollector = dataCollector;
    }

    @PostConstruct
    public void demonstrateConcurrentAccess() {
        System.out.println("=== Тестирование многопоточного доступа ===");

        for (int i = 0; i < 3; i++) {
            int threadId = i;
            new Thread(() -> processItems(threadId)).start();
        }
    }

    private void processItems(int threadId) {
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            String key = "key-" + (i % 3); // Создаем дубликаты
            Item item = new Item(key, "data-from-" + threadId + "-" + i);
            dataCollector.collectItem(item);

            try {
                Thread.sleep(random.nextInt(100));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        dataCollector.printStatistics();
    }
}
