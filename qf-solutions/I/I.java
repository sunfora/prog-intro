import java.util.Scanner;

public class I {
    public static void main(String[] args) {
        int xl = Integer.MAX_VALUE;
        int yl = Integer.MAX_VALUE;
        int xr = Integer.MIN_VALUE;
        int yr = Integer.MIN_VALUE;
        Scanner scanner = new Scanner(System.in);
        try {
            int n = scanner.nextInt();
            for (int i = 0; i < n; ++i) {
                int xi = scanner.nextInt();
                int yi = scanner.nextInt();
                int hi = scanner.nextInt();
                xl = Math.min(xl, xi - hi);
                xr = Math.max(xr, xi + hi);
                yl = Math.min(yl, yi - hi);
                yr = Math.max(yr, yi + hi);
            }
        } finally {
            scanner.close();
        }
        System.out.print((xl+xr)/2 + " " + (yl + yr)/2 + " " + (Math.max(xr - xl, yr - yl) + 1)/2);
    }
}