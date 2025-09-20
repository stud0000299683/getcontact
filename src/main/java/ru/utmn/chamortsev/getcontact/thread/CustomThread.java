package ru.utmn.chamortsev.getcontact.thread;

public class CustomThread extends Thread {
    private String threadName;

    public CustomThread(String threadName) {
        this.threadName = threadName;
    }

    @Override
    public void run() {
        System.out.println("Поток " + threadName + " запущен");
        try {
            for (int i = 0; i < 3; i++) {
                System.out.println(threadName + " - выполнение " + (i + 1));
                Thread.sleep(1500);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Поток " + threadName + " завершен");
    }
}