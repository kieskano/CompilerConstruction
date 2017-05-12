package pp.block3.cp.lockcoupling;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Wijtse on 11-5-2017.
 */
public class MySlowSafeLinkedList<T> implements List<T> {

    private Lock lock = new ReentrantLock(false);
    private MyNode<T> startNode;

    @Override
    public void insert(int pos, T val) {
        lock.lock();
        MyNode<T> newNode = new MyNode<>(val);
        MyNode<T> curNode = startNode;
        for (int i = 1; i < pos; i++) {
            curNode = curNode.getNext();
        }
        newNode.setNext(curNode.getNext());
        curNode.setNext(newNode);
        lock.unlock();
    }

    @Override
    public void add(T val) {
        lock.lock();
        MyNode<T> newNode = new MyNode<>(val);
        MyNode<T> curNode = startNode;
        if (curNode != null) {
            while (curNode.getNext() != null) {
                curNode = curNode.getNext();
            }
            curNode.setNext(newNode);
        } else {
            startNode = newNode;
        }
        lock.unlock();
    }

    @Override
    public void remove(T item) {
        lock.lock();
        MyNode<T> curNode = startNode;
        MyNode<T> prevNode = null;
        if (curNode.getValue().equals(item)) {
            startNode = curNode.getNext();
            lock.unlock();
            return;
        }

        while (curNode.getNext() != null) {
            prevNode = curNode;
            curNode = curNode.getNext();

            if (curNode.getValue().equals(item)) {
                prevNode.setNext(curNode.getNext());
                lock.unlock();
                return;
            }
        }
        lock.unlock();
    }

    @Override
    public void delete(int pos) {
        lock.lock();
        MyNode<T> curNode = startNode;
        MyNode<T> prevNode = null;
        if (pos == 0) {
            startNode = curNode.getNext();
            lock.unlock();
            return;
        }
        for (int i = 0; i < pos; i++) {
            prevNode = curNode;
            curNode = curNode.getNext();
        }
        prevNode.setNext(curNode.getNext());
        lock.unlock();
    }

    @Override
    public int size() {
        lock.lock();
        int size = 0;
        MyNode<T> curNode = startNode;
        while (curNode != null) {
            curNode = curNode.getNext();
            size++;
        }
        lock.unlock();
        return size;
    }
}
