package com.utmn.chamortsev.hw1.controller;

import com.utmn.chamortsev.hw1.service.CustomThreadService;
import com.utmn.chamortsev.hw1.service.ResultService;
import com.utmn.chamortsev.hw1.task.TaskService;
import com.utmn.chamortsev.hw1.service.ThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/threads")
public class ThreadController {

    @Autowired
    private ThreadService threadService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private CustomThreadService customThreadService;

    @Autowired
    private ResultService resultService;

    @GetMapping("/start")
    public String startThread() {
        threadService.runSimpleThread();
        return "Поток запущен!";
    }

    @GetMapping("/multiple")
    public String startMultipleThreads() {
        threadService.runMultipleThreads();
        return "Несколько потоков запущены!";
    }

    @GetMapping("/task/{name}")
    public String runTask(@PathVariable String name) {
        taskService.executeSimpleTask(name);
        return "Задача '" + name + "' запущена!";
    }

    @GetMapping("/calculate/{number}")
    public String calculate(@PathVariable int number) {
        return taskService.executeCalculationTask(number);
    }

    @GetMapping("/parallel")
    public String runParallelTasks() {
        taskService.executeParallelTasks();
        return "Параллельные задачи запущены!";
    }

    @GetMapping("/async")
    public String asyncExample() {
        Thread asyncThread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Асинхронная задача начата");
                try {
                    Thread.sleep(3000);
                    System.out.println("Асинхронная задача завершена");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        asyncThread.start();

        return "Асинхронная задача запущена, основной поток свободен!";
    }

    // ДОБАВЬТЕ ЭТОТ КОД СЮДА:
    @GetMapping("/custom/{name}")
    public String startCustomThread(@PathVariable String name) {
        customThreadService.startCustomThread(name);
        return "Кастомный поток '" + name + "' запущен!";
    }

    @GetMapping("/custom-multiple")
    public String startMultipleCustomThreads() {
        customThreadService.startMultipleCustomThreads();
        return "Несколько кастомных потоков запущены!";
    }
}