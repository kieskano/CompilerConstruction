package pp.block4.cc.cp;

/**
 * Created by Wijtse on 4-5-2017.
 */
public class NewQueue implements Queue{

    /*
        This class is thread safe because the critical sections are turned into atomic operations, so multiple instances
        can not be run at the same time. This ensures that everything happens sequential so it is thread safe.
     */
    private Node startNode;
    private Node endNode;
    private int length = 0;

    @Override
    public synchronized void push(Object x) {
        Node newNode = new Node(x);
        if (startNode == null) {
            startNode = newNode;
            endNode = newNode;
            length = 1;
        } else {
            endNode.setNext(newNode);
            endNode = newNode;
            length++;
        }
    }

    @Override
    public synchronized Object pull() throws QueueEmptyException {
        if (startNode == null) {
            throw new QueueEmptyException();
        }
        Object result = startNode.getValue();
        startNode = startNode.getNext();
        length--;
        return result;
    }

    @Override
    public synchronized int getLength() {
        return length;
    }
}
