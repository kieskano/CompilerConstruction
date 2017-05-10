package pp.block2.cp.queue;

/**
 * Created by Wijtse on 4-5-2017.
 */
public class MyQueue implements Queue{

    private Node startNode;
    private Node endNode;
    private int length = 0;

    @Override
    public void push(Object x) {
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
    public Object pull() throws QueueEmptyException {
        if (startNode == null) {
            throw new QueueEmptyException();
        }
        Object result = startNode.getValue();
        startNode = startNode.getNext();
        length--;
        return result;
    }

    @Override
    public int getLength() {
        return length;
    }
}
