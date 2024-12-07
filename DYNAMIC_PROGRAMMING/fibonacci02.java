package DYNAMIC_PROGRAMMING;
import java.io.*;

public class fibonacci02 {
    // Custom input methods
    @SafeVarargs
    private static <T> void see(T... args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        for (T arg : args) {
            if (arg instanceof Integer) {
                arg = (T) Integer.valueOf(br.readLine());
            } else if (arg instanceof Long) {
                arg = (T) Long.valueOf(br.readLine());
            } else if (arg instanceof Double) {
                arg = (T) Double.valueOf(br.readLine());
            } else if (arg instanceof String) {
                arg = (T) br.readLine();
            }
        }
    }

    @SafeVarargs
    private static <T> void put(T... args) {
        for (T arg : args) {
            System.out.print(arg + " ");
        }
        System.out.println();
    }

    @SafeVarargs
    private static <T> void putl(T... args) {
        for (T arg : args) {
            System.out.print(arg + " ");
        }
        System.out.println();
    }

    private static void error(String... args) {
        String _s = String.join(" ", args);
        err(_s.split(" "));
    }

    private static void err(String[] it) {
        for (String s : it) {
            System.err.print(s + " ");
        }
        System.err.println();
    }

    // Custom macros
    private static final long inf = Integer.MAX_VALUE;
    private static final double ep = 0.0000001;
    private static final double pi = Math.acos(-1.0);
    private static final long md = 1000000007;

    static class Pair {
        long first;
        long second;

        Pair(long first, long second) {
            this.first = first;
            this.second = second;
        }
    }

    private static Pair fib(long n, long md) {
        if (n == 0) {
            return new Pair(0, 1);
        }
        Pair p = fib(n >> 1, md);
        long c = p.first * (2 * p.second - p.first + md) % md;
        long d = (p.first * p.first % md + p.second * p.second % md) % md;
        if ((n & 1) != 0) {
            return new Pair(d, (c + d) % md);
        } else {
            return new Pair(c, d);
        }
    }

    private static void solve() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        long n = Long.parseLong(br.readLine());
        put(fib(n, md).first);
    }

    public static void main(String[] args) throws IOException {
        long t = 1;
        // Uncomment below to read t from input
        // Scanner sc = new Scanner(System.in);
        // t = sc.nextLong();
        while (t-- > 0) {
            solve();
        }
        // Uncomment below to measure execution time
        // long startTime = System.nanoTime();
        // long endTime = System.nanoTime();
        // System.err.printf("Time Taken: %.10f seconds%n", (endTime - startTime) / 1e9);
    }
}
