package ru.utmn.chamortsev.getcontact.service;

import org.springframework.stereotype.Service;
import ru.utmn.chamortsev.getcontact.task.SimpleTask;
import ru.utmn.chamortsev.getcontact.task.CalculationTask;

@Service
public class TaskService {

    public void executeSimpleTask(String taskName) {
        SimpleTask task = new SimpleTask(taskName);
        Thread thread = new Thread((Runnable) task);
        thread.start();
    }

    public String executeCalculationTask(int number) {
        CalculationTask task = new CalculationTask(number);
        Thread thread = new Thread(task);
        thread.start();

        // Ждем завершения потока для получения результата
        try {
            thread.join(); // Ожидаем завершения потока
        } catch (InterruptedException e) {
            e.printStackTrace();
            return "Ошибка ожидания потока";
        }

        return task.getResult();
    }

    public void executeParallelTasks() {
        for (int i = 1; i <= 3; i++) {
            SimpleTask task = new SimpleTask("Параллельная задача " + i);
            Thread thread = new Thread((Runnable) task);
            thread.start();
        }
    }
}