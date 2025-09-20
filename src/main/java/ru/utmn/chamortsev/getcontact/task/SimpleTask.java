package ru.utmn.chamortsev.getcontact.task;

public class SimpleTask implements Runnable{
    private String taskName;
    public SimpleTask(String taskName) {
        this.taskName = taskName;
    }

    @Override
    public void run() {
        System.out.println("Задача '" + taskName + "' начата");
        try {
            for (int i = 0; i < 3; i++) {
                System.out.println(taskName + " - шаг " + (i + 1));
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Задача '" + taskName + "' завершена");
    }
}
