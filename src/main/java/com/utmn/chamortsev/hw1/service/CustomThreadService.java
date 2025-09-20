package com.utmn.chamortsev.hw1.service;

import com.utmn.chamortsev.hw1.thread.CustomThread;
import org.springframework.stereotype.Service;

@Service
public class CustomThreadService {

    public void startCustomThread(String name) {
        CustomThread thread = new CustomThread(name);
        thread.start();
    }

    public void startMultipleCustomThreads() {
        for (int i = 1; i <= 3; i++) {
            CustomThread thread = new CustomThread("Custom-" + i);
            thread.start();
        }
    }
}