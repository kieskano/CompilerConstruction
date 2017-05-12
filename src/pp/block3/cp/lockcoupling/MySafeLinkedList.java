package pp.block3.cp.lockcoupling;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Wijtse on 12-5-2017.
 */
public class MySafeLinkedList<T> implements List<T> {

    private Lock startNodeLock = new ReentrantLock(false);
    private MyNode<T> startNode;

    @Override
    public void insert(int pos, T val) {
        //This function throws nullpointers if list is empty or if the index is out of bounds
        MyNode<T> newNode = new MyNode<>(val);
        startNode.getLock().lock();
        MyNode<T> curNode = startNode;
        MyNode<T> prevNode;
        for (int i = 1; i < pos; i++) {
            curNode.getNext().getLock().lock();
            curNode = curNode.getNext();
        }
        newNode.setNext(curNode.getNext());
        curNode.setNext(newNode);

        //Unlock all the previous nodes
        curNode = startNode;
        for (int i = 0; i < pos; i++) {
            prevNode = curNode;
            curNode = curNode.getNext();
            prevNode.getLock().unlock();
        }
    }

    @Override
    public void add(T val) {
        MyNode<T> newNode = new MyNode<>(val);
        startNodeLock.lock();
        MyNode<T> curNode = startNode;
        MyNode<T> prevNode;
        if (curNode != null) {

            curNode.getLock().lock();
            startNodeLock.unlock();
            while (curNode.getNext() != null) {
                curNode.getNext().getLock().lock();
                prevNode = curNode;
                curNode = curNode.getNext();
                prevNode.getLock().unlock();
            }
            curNode.setNext(newNode);
            curNode.getLock().unlock();
        } else {
            startNode = newNode;
            startNodeLock.unlock();
        }
    }

    @Override
    public void remove(T item) {
        startNodeLock.lock();
        MyNode<T> curNode = startNode;
        MyNode<T> prevNode = null;
        if (curNode != null) {

            curNode.getLock().lock();

            if (curNode.getValue().equals(item)) {
                startNode = curNode.getNext();
                curNode.getLock().unlock();
                startNodeLock.unlock();
                return;
            } else {
                startNodeLock.unlock();
            }
        } else {
            startNodeLock.unlock();
        }

        try {
            curNode.getNext().getLock().lock();
        } catch (NullPointerException e) {}

        while (curNode.getNext() != null) {
            if (prevNode != null) prevNode.getLock().unlock();
            prevNode = curNode;
            curNode = curNode.getNext();

            if (curNode.getValue().equals(item)) {
                try {
                    curNode.getNext().getLock().lock();
                } catch (NullPointerException e) {}

                prevNode.setNext(curNode.getNext());
                prevNode.getLock().unlock();
                try {
                    prevNode.getNext().getLock().unlock();
                } catch (NullPointerException e) {}
                return;
            }
            try {
                curNode.getNext().getLock().lock();
            } catch (NullPointerException e) {}
        }
        prevNode.getLock().unlock();
        curNode.getLock().unlock();
    }

    @Override
    public void delete(int pos) {
        startNodeLock.lock();
        startNode.getLock().lock();
        MyNode<T> curNode = startNode;
        MyNode<T> prevNode = null;
        if (pos == 0) {
            startNode = curNode.getNext();
            curNode.getLock().unlock();
            startNodeLock.unlock();
            return;
        } else {
            startNodeLock.unlock();
        }
        for (int i = 0; i < pos; i++) {
            prevNode = curNode;
            curNode.getNext().getLock().lock();
            curNode = curNode.getNext();
        }
        prevNode.setNext(curNode.getNext());

        curNode = startNode;
        //Unlock all the locks
        for (int i = 0; i < pos; i++) {
            prevNode = curNode;
            curNode = curNode.getNext();
            prevNode.getLock().unlock();
        }
    }

    @Override
    public int size() {
        int size = 0;
        startNodeLock.lock();

        try {
            startNode.getLock().lock();
        } catch (NullPointerException e) {}

        MyNode<T> curNode = startNode;
        MyNode<T> prevNode = startNode;
        startNodeLock.unlock();
        while (curNode != null) {

            try {
                curNode.getNext().getLock().lock();
            } catch (NullPointerException e) {}

            curNode = curNode.getNext();
            size++;
        }

        if (size > 0) {
            curNode = startNode;
            while (curNode.getNext() != null) {
                prevNode = curNode;
                curNode = curNode.getNext();
                prevNode.getLock().unlock();
            }
            curNode.getLock().unlock();
        }
        return size;
    }
}
