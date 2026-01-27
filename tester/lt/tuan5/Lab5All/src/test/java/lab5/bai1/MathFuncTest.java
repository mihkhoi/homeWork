
package lab5.bai1;

import org.junit.*;
import static org.junit.Assert.*;

public
class MathFuncTest {

  private
    MathFunc mf;

    @Before public void setUp() {
        mf = new MathFunc();
    }

    @After public void tearDown() {
    }

    @Test public void calls() {
        assertEquals(0, mf.getCalls());
        mf.plus(1, 2);
        assertEquals(1, mf.getCalls());
        mf.factorial(5);
        assertEquals(2, mf.getCalls());
    }

    @Test public void factorial() {
        assertEquals(1L, mf.factorial(0));
        assertEquals(1L, mf.factorial(1));
        assertEquals(120L, mf.factorial(5));
    }

    @Test(expected = IllegalArgumentException.class) public void factorialNegative() {
        mf.factorial(-1);
    }

    @Test public void todo() {
        assertEquals(7, mf.plus(3, 4));
        assertEquals(-1, mf.plus(3, -4));
    }

    @Ignore("Demo ignore")
        @Test public void ignoredCase() {
        fail("Ignore nên không chạy");
    }
}
