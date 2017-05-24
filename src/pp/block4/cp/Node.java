package pp.block4.cp;

/**
 * Created by Wijtse on 4-5-2017.
 */
public class Node {

    private Node next;
    private Object value;

    public Node(Object value) {
        this.value = value;
    }

    public Node(Object value, Node next) {
        this.value = value;
        this.next = next;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

}
