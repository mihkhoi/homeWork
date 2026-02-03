package fpoly;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public
class PersonTest {

    // Cách 2: Rule
    @Rule public ExpectedException exception = ExpectedException.none();

    // Cách 1: @Test(expected)
    @Test(expected = IllegalArgumentException.class) public void testExpectedAnnotation_ageNegative() {
        new Person("Poly", -1);
    }

    // Cách 2: ExpectedException Rule
    @Test public void testExpectedRule_ageNegative() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Invalid age");
        new Person("Poly", -5);
    }

    // Cách 3: try-catch
    @Test public void testTryCatch_ageNegative() {
        try {
            new Person("Poly", -10);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Invalid age"));
        }
    }
}
