package pp;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Wijtse on 11-5-2017.
 */
public class ThreadTest extends Thread {
    public void run() {
        System.out.println("Ho");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Hai");
    }

    public static void main(String[] args) {
//        Thread t = new ThreadTest();
//        t.start();
//        t.start();
        Lock l = new ReentrantLock(true);
        l.lock();
        l.lock();
        l.unlock();
        l.unlock();
        l.unlock();
        System.out.println("Hai");
    }
}
