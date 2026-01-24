package labunit;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class Bai2_PolynomialTests {

    @Test
    void nNegative_throwInvalidData() {
        var ex = assertThrows(IllegalArgumentException.class,
                () -> new Polynomial(-1, List.of(1)));
        assertEquals("Invalid Data", ex.getMessage());
    }

    @Test
    void wrongCoefficientCount_throwInvalidData() {
        var ex = assertThrows(IllegalArgumentException.class,
                () -> new Polynomial(2, List.of(1, 2))); // thiếu n+1=3 hệ số
        assertEquals("Invalid Data", ex.getMessage());
    }

    @Test
    void cal_correctValue() {
        // 1 + 2x + 3x^2
        var p = new Polynomial(2, List.of(1, 2, 3));
        assertEquals(17, p.cal(2));
        assertEquals(6, p.cal(1));
        assertEquals(1, p.cal(0));
    }
}
