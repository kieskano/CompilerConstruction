package pp.block2.cp;

import nl.utwente.pp.cp.junit.ConcurrentRunner;
import nl.utwente.pp.cp.junit.Threaded;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import pp.block2.cp.queue.MyQueue;
import pp.block2.cp.queue.MySafeQueue;
import pp.block2.cp.queue.QueueEmptyException;

/**
 * Created by han on 4-5-17.
 */
@RunWith(ConcurrentRunner.class)
public class MyQueueTest {

    public static final int AMOUNT_OF_THREADS = 2;

    private MySafeQueue queue;
    private MySafeQueue queue2;

    @Before
    public void before() {
        queue = new MySafeQueue();
        queue2 = new MySafeQueue();
        for (int i = 0; i<20; i++){
            queue2.push(1);
        }
    }

    @Test
    @Threaded(count = AMOUNT_OF_THREADS)
    public void test() throws InterruptedException {
        //TEST PUSH
        for (int i = 0; i < 10; i++) {
            queue.push(1);
        }

        //TEST PULL
        for (int i = 0; i < 10; i++) {
            try {
                Object obj = queue2.pull();
            } catch (QueueEmptyException e) {
                e.printStackTrace();
            }
        }
    }

    @After
    public void after() {
        Assert.assertEquals(0, queue2.getLength());
        Assert.assertEquals(20, queue.getLength());
    }


}
