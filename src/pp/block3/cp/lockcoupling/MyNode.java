package pp.block3.cp.lockcoupling;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Wijtse on 11-5-2017.
 */
public class MyNode<T> {

    private T value;
    private Lock lock = new ReentrantLock(true);
    private MyNode next;

    public MyNode(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public Lock getLock() {
        return lock;
    }

    public MyNode getNext() {
        return next;
    }

    public void setNext(MyNode next) {
        this.next = next;
    }
}
