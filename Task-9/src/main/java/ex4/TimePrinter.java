package ex4;

import java.time.LocalDateTime;

public class TimePrinter extends Thread {

    private int interval;

    public TimePrinter(int n) {
        this.interval=n;
        setDaemon(true);
    }

    @Override
    public void run() {
        while (true) {
            System.out.println(LocalDateTime.now());
            try {
                Thread.sleep(interval*1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
