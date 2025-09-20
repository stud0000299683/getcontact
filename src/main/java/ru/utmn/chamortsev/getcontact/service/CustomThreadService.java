package ru.utmn.chamortsev.getcontact.service;

import org.springframework.stereotype.Service;
import ru.utmn.chamortsev.getcontact.thread.CustomThread;

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