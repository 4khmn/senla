package ex1;

public class Main {
    private static final Object lock = new Object();


    public static void main(String[] args) throws Exception {
        Thread thread2 = new Thread(() -> {
            synchronized (lock) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread thread1 = new Thread(() -> {
            try {
                synchronized (lock) {
                    lock.wait();
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });



        thread2.start();
        //new
        System.out.println(thread1.getState());
        thread1.start();
        //runnable
        System.out.println(thread1.getState());
        Thread.sleep(100);
        //blocked
        System.out.println(thread1.getState());


        Thread.sleep(2000);
        //waiting
        System.out.println(thread1.getState());

        synchronized (lock) {
            lock.notify();
        }

        Thread.sleep(100);
        //timed_waiting
        System.out.println(thread1.getState());

        thread1.join();
        //terminated
        System.out.println(thread1.getState());
    }
}