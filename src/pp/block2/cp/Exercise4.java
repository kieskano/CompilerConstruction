package pp.block2.cp;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Wijtse on 7-5-2017.
 */
public class Exercise4 {

    private static final int NUMBER_OF_THREADS = 1;
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public static void main(String[] args) {
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            Thread thread = new Thread(){
                public void run() {
                    String date = ((int) (Math.random() * 31)) + "/" + ((int) (Math.random() * 12)) + "/" + ((int) (Math.random() * 3000));
                    System.out.println(date);
                    Date result = sdf.parse(date, new ParsePosition(0));
                    System.out.println("Expected: " + date + " Actual: " + result.toString());
                }
            };
            threads.add(thread);
        }

        for (Thread thread : threads) {
            thread.start();
        }
    }
}
