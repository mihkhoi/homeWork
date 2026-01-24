package labunit;

public class PowerUtil {
    public static double power(double x, int n) {
        if (n == 0) return 1.0;

        // tránh chia 0 khi n âm
        if (x == 0.0 && n < 0) {
            throw new ArithmeticException("Divide by zero");
        }

        if (n > 0) return x * power(x, n - 1);
        return power(x, n + 1) / x; // n < 0
    }
}
