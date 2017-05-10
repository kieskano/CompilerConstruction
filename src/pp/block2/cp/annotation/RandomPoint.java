package pp.block2.cp.annotation;

import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

public class RandomPoint {

    public static final int NUMBER_OF_THREADS = 20;
    public static final int NUMBER_OF_ITERATIONS = 20;
    public static final int INCREMENT_VALUE = 5;

    public static void main(String[] args) {
        while (true) {
            Point p = new Point();

            List<RandomDrift> list = new ArrayList<>();
            for (int i = 0; i < NUMBER_OF_THREADS; i++) {
                RandomDrift r = new RandomDrift(p, NUMBER_OF_ITERATIONS, INCREMENT_VALUE);
                list.add(r);
            }

            for (RandomDrift r : list) {
                r.start();
            }
            for (RandomDrift r : list) {
                try {
                    r.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            Assert.assertEquals(NUMBER_OF_THREADS * NUMBER_OF_ITERATIONS * INCREMENT_VALUE, p.getX());
            Assert.assertEquals(NUMBER_OF_THREADS * NUMBER_OF_ITERATIONS * INCREMENT_VALUE, p.getY());
        }
    }
}

//package pp.block2.cp.annotation;
//
//public class RandomPoint {
//    public static void main(String[] args) {
//        Point p = new Point();
//        RandomDrift r1 = new RandomDrift(p);
//        RandomDrift r2 = new RandomDrift(p);
//        r1.start();
//        r2.start();
//    }
//}