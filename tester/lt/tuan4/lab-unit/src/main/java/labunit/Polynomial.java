package labunit;

import java.util.List;

public class Polynomial {
    private final int n;
    private final List<Integer> a;

    public Polynomial(int n, List<Integer> a) {
        if (n < 0 || a == null || a.size() != n + 1) {
            throw new IllegalArgumentException("Invalid Data");
        }
        this.n = n;
        this.a = List.copyOf(a);
    }

    // giống code trong ảnh: ép về int
    public int cal(double x) {
        int result = 0;
        for (int i = 0; i <= this.n; i++) {
            result += (int) (a.get(i) * Math.pow(x, i));
        }
        return result;
    }
}
