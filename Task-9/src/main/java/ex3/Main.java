package ex3;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;
import java.util.Random;

public class Main {
    private static Object lock = new Object();
    private static int MAX_SIZE = 10;
    private static Deque<Integer> queue = new ArrayDeque<>();
    public static void main(String[] args) {
        Thread producer = new Thread(() -> {
            Random random = new Random();
            while (true) {
                synchronized (lock) {
                    while(queue.size()==MAX_SIZE){
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    int rand = random.nextInt(100);
                    queue.push(rand);
                    System.out.println("Produced: " + rand);
                    lock.notify();
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread consumer = new Thread(() -> {
           while(true){
               synchronized (lock) {
                   while (queue.size() == 0) {
                       try {
                           lock.wait();
                       } catch (InterruptedException e) {
                       }
                   }
                   int value = queue.pop();
                   System.out.println("Consumed: " + value);
                   lock.notify();
               }
               try {
                   Thread.sleep(150);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
        });


        producer.start();
        consumer.start();
    }
}
