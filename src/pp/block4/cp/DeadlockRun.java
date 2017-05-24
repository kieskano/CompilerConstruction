package pp.block4.cp;

/**
 * Created by han on 19-5-17. Deadlock runnable
 */
public class DeadlockRun implements Runnable{

    public static final int RUN = 10000000;
    private LeftRightDeadlock lrd;

    public DeadlockRun(LeftRightDeadlock lrd) {
        this.lrd = lrd;
    }

    public static void main(String[] args) {
        LeftRightDeadlock lrd = new LeftRightDeadlock();
        Thread t1 = new Thread(new DeadlockRun(lrd));
        Thread t2 = new Thread(new DeadlockRun(lrd));
        t1.start();
        t2.start();
    }

    @Override
    public void run() {
        for (int i = 0; i < RUN; i++) {
            lrd.leftRight();
            lrd.rightLeft();
        }
    }
}
