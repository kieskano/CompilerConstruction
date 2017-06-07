package pp.block5.cc;

/**
 * Created by Wijtse on 1-6-2017.
 */
public class testPrime {
    public static void main(String[] args) {
        int x = 15;
        int i = 2;
        boolean stop = false;
        while (!stop && i*i < x) {
            stop = i * (x/i) == x;
            i = i+1;
        }
        if (stop) {
            System.out.println("Divisor: " + (i-1));
        } else {
            System.out.println("Is prime " + stop);
        }
    }
}
