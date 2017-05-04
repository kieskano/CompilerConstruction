package pp.block2.cp;

import nl.utwente.pp.cp.junit.ConcurrentRunner;
import nl.utwente.pp.cp.junit.Threaded;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by han on 4-5-17.
 */
@RunWith(ConcurrentRunner.class)
public class UnsafeSequenceTest {

    public static final int AMOUNT_OF_THREADS = 2;

    private UnsafeSequence unss;
    private Lock l;
    private Object monitor;
    private Boolean available;

    @Before
    public void before() {
        unss = new UnsafeSequence();
        l = new ReentrantLock();
        monitor = new Object();
        available = true;
    }

    @Test
    @Threaded(count = AMOUNT_OF_THREADS)
    public void unsafeTest() throws InterruptedException {
       for (int i = 0; i < 10; i++) {
           int n = unss.getNext();
           System.out.println(n);
       }
    }


    @Test
    @Threaded(count = AMOUNT_OF_THREADS)
    public void safe1Test() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            synchronized (unss) {
                int n = unss.getNext();
                System.out.println(n);
            }
        }
    }


    @Test
    @Threaded(count = AMOUNT_OF_THREADS)
    public void safe2Test() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            l.lock();
            int n = unss.getNext();
            System.out.println(n);
            l.unlock();
        }
    }


    @Test
    @Threaded(count = AMOUNT_OF_THREADS)
    public void safe3Test() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            synchronized (monitor) {
                while (!available) {
                    monitor.wait();
                }
            }
            int n = unss.getNext();
            System.out.println(n);
            synchronized (monitor) {
                monitor.notifyAll();
            }
        }
    }


    @After
    public void after() {
        Assert.assertEquals(20, unss.getNext());
    }


}
