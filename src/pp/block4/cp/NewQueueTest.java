package pp.block4.cp;

import nl.utwente.pp.cp.junit.ConcurrentRunner;
import nl.utwente.pp.cp.junit.Threaded;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by han on 4-5-17. new queue
 */
@RunWith(ConcurrentRunner.class)
public class NewQueueTest {

    public static final int AMOUNT_OF_THREADS = 5;
    public static final int MULT = 1000000;
    public static final int AMOUNT = MULT * AMOUNT_OF_THREADS;

    private NewQueue queue;
    private NewQueue queue2;

    private CLQueue queue3;
    private CLQueue queue4;

    @Before
    public void before() {
        queue = new NewQueue();
        queue2 = new NewQueue();
        queue3 = new CLQueue();
        queue4 = new CLQueue();
        for (int i = 0; i<AMOUNT; i++){
            queue2.push(1);
        }
        for (int i = 0; i<AMOUNT; i++){
            queue4.push(1);
        }

    }

    @Test
    @Threaded(count = AMOUNT_OF_THREADS)
    public void test() throws InterruptedException {

        Thread t = Thread.currentThread();
        String name = t.getName();
        long startTime;
        long endTime;
        long timeTaken;

        //TEST PUSH only to new
        startTime = System.currentTimeMillis();
        for (int i = 0; i < AMOUNT/AMOUNT_OF_THREADS; i++) {
            queue.push(1);
        }
        endTime = System.currentTimeMillis();
        timeTaken = endTime - startTime;
        System.out.println("test: Thread " + name + " took " + timeTaken + "ms to push to NewQueue.");

        //TEST PULL only from new
        startTime = System.currentTimeMillis();
        for (int i = 0; i < AMOUNT/AMOUNT_OF_THREADS; i++) {
            try {
                queue2.pull();
            } catch (QueueEmptyException e) {
                e.printStackTrace();
            }
        }
        endTime = System.currentTimeMillis();
        timeTaken = endTime - startTime;
        System.out.println("test: Thread " + name + " took " + timeTaken + "ms to pull from NewQueue.");

        //TEST PUSH only to cl
        startTime = System.currentTimeMillis();
        for (int i = 0; i < AMOUNT/AMOUNT_OF_THREADS; i++) {
            queue3.push(1);
        }
        endTime = System.currentTimeMillis();
        timeTaken = endTime - startTime;
        System.out.println("test: Thread " + name + " took " + timeTaken + "ms to push to CLQueue.");

        //TEST PULL only from cl
        startTime = System.currentTimeMillis();
        for (int i = 0; i < AMOUNT/AMOUNT_OF_THREADS; i++) {
            try {
                queue4.pull();
            } catch (QueueEmptyException e) {
                e.printStackTrace();
            }
        }
        endTime = System.currentTimeMillis();
        timeTaken = endTime - startTime;
        System.out.println("test: Thread " + name + " took " + timeTaken + "ms to pull from CLQueue.");

        //TEST PUSH and PULL new
        startTime = System.currentTimeMillis();
        for (int i = 0; i < AMOUNT/AMOUNT_OF_THREADS; i++) {
            queue2.push(1);
            try {
                queue2.pull();
            } catch (QueueEmptyException e) {
                e.printStackTrace();
            }
        }
        endTime = System.currentTimeMillis();
        timeTaken = endTime - startTime;
        System.out.println("test: Thread " + name + " took " + timeTaken + "ms to push and pull NewQueue.");

        //TEST PUSH and PULL cl
        startTime = System.currentTimeMillis();
        for (int i = 0; i < AMOUNT/AMOUNT_OF_THREADS; i++) {
            queue4.push(1);
            try {
                queue4.pull();
            } catch (QueueEmptyException e) {
                e.printStackTrace();
            }
        }
        endTime = System.currentTimeMillis();
        timeTaken = endTime - startTime;
        System.out.println("test: Thread " + name + " took " + timeTaken + "ms to push and pull CLQueue.");
    }

    @After
    public void after() {
        Assert.assertEquals(0, queue2.getLength());
        Assert.assertEquals(AMOUNT, queue.getLength());
        Assert.assertEquals(0, queue4.getLength());
        Assert.assertEquals(AMOUNT, queue3.getLength());
    }


    /*
    There is no need for locks in CLQueue because the ConcurrentLinkedList already implements them.

    A large test with 5 threads, 5000000 elements (sucessfull):

    test: Thread pool-1-thread-4 took 1699ms to push to NewQueue.
    test: Thread pool-1-thread-4 took 83ms to pull from NewQueue.
    test: Thread pool-1-thread-4 took 26ms to push to CLQueue.
    test: Thread pool-1-thread-1 took 1837ms to push to NewQueue.
    test: Thread pool-1-thread-4 took 37ms to pull from CLQueue.
    test: Thread pool-1-thread-2 took 2214ms to push to NewQueue.
    test: Thread pool-1-thread-1 took 383ms to pull from NewQueue.
    test: Thread pool-1-thread-3 took 2231ms to push to NewQueue.
    test: Thread pool-1-thread-5 took 2235ms to push to NewQueue.
    test: Thread pool-1-thread-1 took 20ms to push to CLQueue.
    test: Thread pool-1-thread-1 took 31ms to pull from CLQueue.
    test: Thread pool-1-thread-5 took 223ms to pull from NewQueue.
    test: Thread pool-1-thread-5 took 17ms to push to CLQueue.
    test: Thread pool-1-thread-3 took 259ms to pull from NewQueue.
    test: Thread pool-1-thread-2 took 291ms to pull from NewQueue.
    test: Thread pool-1-thread-5 took 43ms to pull from CLQueue.
    test: Thread pool-1-thread-3 took 51ms to push to CLQueue.
    test: Thread pool-1-thread-2 took 53ms to push to CLQueue.
    test: Thread pool-1-thread-3 took 57ms to pull from CLQueue.
    test: Thread pool-1-thread-2 took 55ms to pull from CLQueue.
    test: Thread pool-1-thread-4 took 930ms to push and pull NewQueue.
    test: Thread pool-1-thread-4 took 1045ms to push and pull CLQueue.
    test: Thread pool-1-thread-5 took 1641ms to push and pull NewQueue.
    test: Thread pool-1-thread-1 took 1934ms to push and pull NewQueue.
    test: Thread pool-1-thread-3 took 1627ms to push and pull NewQueue.
    test: Thread pool-1-thread-2 took 1629ms to push and pull NewQueue.
    test: Thread pool-1-thread-5 took 325ms to push and pull CLQueue.
    test: Thread pool-1-thread-3 took 585ms to push and pull CLQueue.
    test: Thread pool-1-thread-1 took 615ms to push and pull CLQueue.
    test: Thread pool-1-thread-2 took 580ms to push and pull CLQueue.


    */

}
