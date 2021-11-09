import java.util.Scanner;

public class B {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n;
        try {
            n = scanner.nextInt();
        } finally {
            scanner.close();
        }
        int start = -25000;
        int end = n + start;
        for (int i = start; i < end; ++i) {
            System.out.println(i * 710);
        }
    }
}