package pp.block2.cp.annotation;

public class RandomDrift extends Thread {
    private Point p;
    private int iterations;
    private int increment;

    public RandomDrift(Point p, int iterations, int increment) {
        this.p = p;
        this.iterations = iterations;
        this.increment = increment;
    }

    @Override
	public void run() {
        for (int i = 0; i < iterations; i++) {
            int xBefore = p.getX();

            p.moveX(increment);

            int xAfter = p.getX();
            System.out.println("XBEFORE=" + xBefore + "\tADDITION=" + increment + "\tAFTER=" + xAfter);
            if (xAfter < xBefore + increment) {
                System.out.println("ERROR HERE!");
                System.exit(1);
            }

            int yBefore = p.getY();

            p.moveY(increment);

            int yAfter = p.getY();
            System.out.println("YBEFORE=" + yBefore + "\tADDITION=" + increment + "\tAFTER=" + yAfter);
            if (yAfter < yBefore + increment) {
                System.out.println("ERROR HERE!");
                System.exit(1);
            }
        }
    }
}
//package pp.block2.cp.annotation;
//
//public class RandomDrift extends Thread {
//    private Point p;
//
//    public RandomDrift(Point p) {
//        this.p = p;
//    }
//
//    @Override
//    public void run() {
//        while (true) {
//            int n = (int) (Math.random() * 10);
//            p.moveX(n);
//            int m = (int) (Math.random() * 10);
//            p.moveY(m);
//        }
//    }
//}