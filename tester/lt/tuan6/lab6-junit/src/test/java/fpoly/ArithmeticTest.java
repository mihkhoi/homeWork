package fpoly;

import org.junit.Test;
import static org.junit.Assert.*;

public
class ArithmeticTest {

    @Test(expected = ArithmeticException.class) public void testDivideByZero_shouldThrow() {
        new JunitMessage("fpoly exception").printMessage();
    }

    @Test public void testPrintHiMessage_shouldReturnHiPrefix() {
        assertEquals("Hi fpoly exception",
                     new JunitMessage("fpoly exception").printHiMessage());
    }
}
