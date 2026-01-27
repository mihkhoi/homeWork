
package lab5.bai1;

public
class MathFunc {
  private
    int calls = 0;

  public
    int getCalls() {
        return calls;
    }

  public
    int plus(int a, int b) {
        calls++;
        return a + b;
    }

  public
    long factorial(int n) {
        calls++;
        if (n < 0)
            throw new IllegalArgumentException("n must be >= 0");
        long r = 1;
        for (int i = 2; i <= n; i++)
            r *= i;
        return r;
    }
}
