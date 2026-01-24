package labunit;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Bai1_PowerTests {

    @Test
    void nEquals0_return1() {
        assertEquals(1.0, PowerUtil.power(2, 0), 1e-9);
        assertEquals(1.0, PowerUtil.power(-5, 0), 1e-9);
    }

    @Test
    void nPositive_works() {
        assertEquals(8.0, PowerUtil.power(2, 3), 1e-9);
        assertEquals(-8.0, PowerUtil.power(-2, 3), 1e-9);
        assertEquals(16.0, PowerUtil.power(-2, 4), 1e-9);
    }

    @Test
    void nNegative_works() {
        assertEquals(0.125, PowerUtil.power(2, -3), 1e-9);
        assertEquals(-0.125, PowerUtil.power(-2, -3), 1e-9);
    }

    @Test
    void zeroNegative_throw() {
        assertThrows(ArithmeticException.class, () -> PowerUtil.power(0, -1));
    }
}
