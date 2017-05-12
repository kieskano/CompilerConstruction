package pp.block3.cp.lockcoupling;

/**
 * Created by Wijtse on 11-5-2017.
 */
public class MyLinkedList<T> implements List<T> {

    private MyNode<T> startNode;

    @Override
    public void insert(int pos, T val) {
        MyNode<T> newNode = new MyNode<>(val);
        MyNode<T> curNode = startNode;
        for (int i = 1; i < pos; i++) {
            curNode = curNode.getNext();
        }
        newNode.setNext(curNode.getNext());
        curNode.setNext(newNode);
    }

    @Override
    public void add(T val) {
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
    }

    @Override
    public void remove(T item) {
        MyNode<T> curNode = startNode;
        MyNode<T> prevNode = null;
        if (curNode.getValue().equals(item)) {
            startNode = curNode.getNext();
            return;
        }

        while (curNode.getNext() != null) {
            prevNode = curNode;
            curNode = curNode.getNext();

            if (curNode.getValue().equals(item)) {
                prevNode.setNext(curNode.getNext());
                return;
            }
        }
    }

    @Override
    public void delete(int pos) {
        MyNode<T> curNode = startNode;
        MyNode<T> prevNode = null;
        if (pos == 0) {
            startNode = curNode.getNext();
            return;
        }
        for (int i = 0; i < pos; i++) {
            prevNode = curNode;
            curNode = curNode.getNext();
        }
        prevNode.setNext(curNode.getNext());
    }

    @Override
    public int size() {
        int size = 0;
        MyNode<T> curNode = startNode;
        while (curNode != null) {
            curNode = curNode.getNext();
            size++;
        }
        return size;
    }
}
