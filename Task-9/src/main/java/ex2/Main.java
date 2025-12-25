package ex2;

public class Main {
    private static final Object lock = new Object();
    private static boolean turnOfFirst = true;
    public static void main(String[] args) {

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                synchronized (lock) {
                    while(!turnOfFirst) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println(Thread.currentThread().getName());
                    turnOfFirst = false;
                    lock.notify();
                }
            }
        }, "Thread-1");

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                synchronized (lock) {
                    while(turnOfFirst) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println(Thread.currentThread().getName());
                    turnOfFirst = true;
                    lock.notify();
                }
            }
        }, "Thread-2");




        thread1.start();
        thread2.start();


    }
}
