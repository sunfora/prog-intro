import java.util.Scanner;


public class D {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        int mod = 998_244_353;
        int n, k;
        try {
            n = scanner.nextInt();
            k = scanner.nextInt();
        } finally {
            scanner.close();
        }
        ModOp op = new ModOp(mod);
        OneWayPalindromes oneWay = new OneWayPalindromes(n, k, op);
        int answer = 0;
        for (int p = 1; p <= n; ++p) {
            for (int j = p; j <= n; j += p) {
                answer = op.add(answer, oneWay.of(p));
            }
        }
        System.out.println(answer);
    }
}

class ModOp {

    int mod;

    public ModOp(int mod) {
        this.mod = mod;
    }

    public int add(int a, int b) {
        return (int)((((long)a)%mod + ((long)b)%mod)%mod + mod)%mod;
    }

    public int sub(int a, int b) {
        return add(a, -b);
    }

    public int mul(int a, int b) {
        return (int)((((long)a)%mod * ((long)b)%mod)%mod + mod)%mod;
    }

    public int pow(int a, int power) {
        if (power < 0) {
            throw new IllegalArgumentException("power < 0");
        }
        int p = a;
        int result = 1;
        while (power > 0) {
            if ((power&1) == 1) {
                result = mul(result, p);
            }
            p = mul(p, p);
            power >>= 1;
        }
        return result;
    }

}

class Ways {

    int[] data;

    public Ways(int n, int k, ModOp op) {
        data = new int[n+1];
        for (int i = 1; i <= n; ++i) {
            data[i] = op.mul((i%2 == 0)? op.mul(i/2, k+1): i, op.pow(k, (i+1)/2));
        }
    }

    public int of(int n) {
        return data[n];
    }
}

class OneWayPalindromes {

    int[] data;
    Ways ways;

    public OneWayPalindromes(int n, int k, ModOp op) {
        data = new int[n+1];
        ways = new Ways(n, k, op);
        for (int p = 1; p <= n; ++p) {
            data[p] = op.sub(ways.of(p), data[p]);
            for (int j = 2; j*p <= n; ++j) {
                data[j * p] = op.add(data[j * p], op.mul(j, data[p]));
            }
        }
    }

    public int of(int n) {
        return data[n];
    }
}