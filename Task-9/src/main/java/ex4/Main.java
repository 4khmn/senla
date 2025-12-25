package ex4;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        TimePrinter timePrinter = new TimePrinter(4);
        timePrinter.start();

        for (int i=0; i<10; i++) {
            System.out.println("main is working");
            Thread.sleep(1400);
        }
    }
}
