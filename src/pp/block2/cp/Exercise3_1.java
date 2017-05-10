package pp.block2.cp;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Wijtse on 7-5-2017.
 */
/*
    The test results imply that the Timer class executes the tasks thread safe. But multiple timers do not take
    each other into account.
 */
public class Exercise3_1 {

    private static int variable = 0;
    private static final int NUMBER_OF_TASKS = 1000;

    public static void main(String[] args) {
        Timer timer = new Timer();
        Date time = new Date(new Date().getTime() + 1000); // Time 1s from now

        for (int i = 0; i < NUMBER_OF_TASKS; i++) {
            TimerTask incTask = new TimerTask() {
                @Override
                public void run() {
                    variable++;
                }
            };
            timer.schedule(incTask, time);
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("OUTPUT=" + variable);
        timer.cancel();
    }


}
