package pp.block4.cc.cp;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by han on 19-5-17.
 */
public class CLQueue implements Queue {

    ConcurrentLinkedQueue clq = new ConcurrentLinkedQueue();

    @Override
    public void push(Object x) {
        clq.add(x);
    }

    @Override
    public Object pull() throws QueueEmptyException {
        Object result;
        if (clq.isEmpty()) {
            throw new QueueEmptyException();
        } else {
            result = clq.poll();
        }
        return result;
    }

    @Override
    public int getLength() {
        return clq.size();
    }
}
