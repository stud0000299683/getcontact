package com.utmn.chamortsev.hw5;

import com.utmn.chamortsev.hw5.BoundedBuffer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BufferRunner implements CommandLineRunner {
    private final BoundedBuffer<Integer> buffer;

    public BufferRunner(BoundedBuffer<Integer> buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run(String... args) throws Exception {
        // Producer поток
        new Thread(() -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    buffer.put(i);
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();

        // Consumer поток
        new Thread(() -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    buffer.take();
                    Thread.sleep(200);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}