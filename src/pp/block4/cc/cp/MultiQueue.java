package pp.block4.cc.cp;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by han on 19-5-17.
 */
public class MultiQueue implements Queue {

    private ConcurrentLinkedQueue queue = null;
    private Map<Long, List<Object>> lists = new ConcurrentHashMap<>();


    @Override
    public void push(Object x) {
        long threadId = Thread.currentThread().getId();

        if (!lists.containsKey(threadId)) {
            lists.put(threadId, new LinkedList<>());
        }

        synchronized (this) {
        }
    }

    @Override
    public Object pull() throws QueueEmptyException {
        long threadId = Thread.currentThread().getId();
        return null;
    }

    @Override
    public int getLength() {
        return 0;
    }
}
