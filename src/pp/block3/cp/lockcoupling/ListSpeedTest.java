package pp.block3.cp.lockcoupling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * Created by Wijtse on 12-5-2017.
 */
public class ListSpeedTest {

    public static void main(String[] args) {
        List<Integer> list = new MySafeLinkedList<>();
        List<Integer> slowList = new MySlowSafeLinkedList<>();

        for (int i = 0; i < 20000; i++) {
            list.add(i);
            slowList.add(i);
        }

        System.out.println("Testing fast list...");
        long startTime = new Date().getTime();
        testList(list);
        long endTime = new Date().getTime();
        System.out.println("Done. Took: " + (endTime - startTime) + " ms");
        System.out.println("Testing slow list...");
        startTime = new Date().getTime();
        testList(slowList);
        endTime = new Date().getTime();
        System.out.println("Done. Took: " + (endTime - startTime) + " ms");
    }

    public static void testList(List<Integer> list) {

        ArrayList<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(){
                @Override
                public void run() {
                    list.add(1);
                }
            };
            threads.add(thread);
        }

        for (int i = 0; i < 10; i++) {
            int finalI = i * 100 + 300;
            Thread thread = new Thread(){
                @Override
                public void run() {
                    list.insert(finalI, 1);
                }
            };
            threads.add(thread);
        }

        for (int i = 0; i < 10; i++) {
            int finalI = i * 100 + 300;
            Thread thread = new Thread(){
                @Override
                public void run() {
                    list.remove(finalI);
                }
            };
            threads.add(thread);
        }

        for (int i = 0; i < 10; i++) {
            int finalI = i * 100 + 300;
            Thread thread = new Thread(){
                @Override
                public void run() {
                    list.delete(finalI);
                }
            };
            threads.add(thread);
        }

        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(){
                @Override
                public void run() {
                    list.size();
                }
            };
            threads.add(thread);
        }
        Collections.shuffle(threads);
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
