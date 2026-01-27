
package lab5.bai4;

import org.junit.Test;
import static org.junit.Assert.*;
import static lab5.bai4.PaymentCalculator.Type.*;

public
class PaymentCalculatorTest {

    @Test public void child_cases() {
        assertEquals(50, PaymentCalculator.calc(CHILD, 0));
        assertEquals(50, PaymentCalculator.calc(CHILD, 17));
    }

    @Test public void male_boundaries() {
        assertEquals(100, PaymentCalculator.calc(MALE, 18));
        assertEquals(100, PaymentCalculator.calc(MALE, 35));
        assertEquals(120, PaymentCalculator.calc(MALE, 36));
        assertEquals(120, PaymentCalculator.calc(MALE, 50));
        assertEquals(140, PaymentCalculator.calc(MALE, 51));
        assertEquals(140, PaymentCalculator.calc(MALE, 145));
    }

    @Test public void female_boundaries() {
        assertEquals(80, PaymentCalculator.calc(FEMALE, 18));
        assertEquals(80, PaymentCalculator.calc(FEMALE, 35));
        assertEquals(110, PaymentCalculator.calc(FEMALE, 36));
        assertEquals(110, PaymentCalculator.calc(FEMALE, 50));
        assertEquals(140, PaymentCalculator.calc(FEMALE, 51));
        assertEquals(140, PaymentCalculator.calc(FEMALE, 145));
    }

    @Test(expected = IllegalArgumentException.class) public void invalid_age_high() {
        PaymentCalculator.calc(MALE, 146);
    }
}
