package com.utmn.chamortsev.hw1.service;

import org.springframework.stereotype.Service;

@Service
public class ThreadService {

    public void runSimpleThread() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    System.out.println("Поток выполняется: " + i);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    public void runMultipleThreads() {
        for (int i = 0; i < 3; i++) {
            final int threadNumber = i;
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Поток " + threadNumber + " запущен");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Поток " + threadNumber + " завершен");
                }
            });
            thread.start();
        }
    }
}