package pp.block5.cc;

/**
 * Created by Wijtse on 1-6-2017.
 */
public class testPrime {
    public static int fib(int n) {
        if (n <= 1) {
            return 1;
        } else {
            return fib(n-2) + fib(n-1);
        }
    }

    public static void main(String[] args) {
        System.out.println("FIB: " + fib(5));
    }
}
