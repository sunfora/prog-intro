import java.util.Scanner;

public class A {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            int a = scanner.nextInt();
            int b = scanner.nextInt();
            int n = scanner.nextInt();
            System.out.println(2*ceil(n - b, b - a) + 1);
        } finally {
            scanner.close();
        }
    }

    public static int ceil(int a, int b) {
        return (a + b - 1)/b;
    }
}