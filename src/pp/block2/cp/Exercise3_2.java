package pp.block2.cp;

import java.util.*;

/**
 * Created by Wijtse on 7-5-2017.
 */
public class Exercise3_2 {

    private static int variable = 0;
    private static final int NUMBER_OF_TIMERS = 1000;

    public static void main(String[] args) {
        Date time = new Date(new Date().getTime() + 1000); // Time 1s from now
        Date time2 = new Date(time.getTime() + 1000);

        List<Timer> timers = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_TIMERS; i++) {
            TimerTask incTask = new TimerTask() {
                @Override
                public void run() {
                    variable++;
                }
            };
            Timer timer = new Timer();
            timer.schedule(incTask, time);
            timers.add(timer);
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("OUTPUT=" + variable);
        for (Timer timer : timers) {
            timer.cancel();
        }

    }


}
