package ru.utmn.chamortsev.getcontact.task;

public class CalculationTask implements Runnable {
    private int number;
    private String result;


    public CalculationTask(int number) {this.number = number;}
    public String getResult() {return result;}

    @Override
    public void run() {
        System.out.println("Вычисление для числа: " + number);
        try {
            Thread.sleep(1500);
            int squared = number * number;
            result = "Квадрат числа " + number + " = " + squared;
            System.out.println(result);
        } catch (InterruptedException e) {
            e.printStackTrace();
            result = "Ошибка вычисления";
        }
    }
}