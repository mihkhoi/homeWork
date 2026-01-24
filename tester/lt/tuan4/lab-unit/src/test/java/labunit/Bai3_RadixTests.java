package labunit;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Bai3_RadixTests {

    @Test
    void ctor_negative_throwIncorrectValue() {
        var ex = assertThrows(IllegalArgumentException.class, () -> new Radix(-1));
        assertEquals("Incorrect Value", ex.getMessage());
    }

    @Test
    void invalidRadix_throwInvalidRadix() {
        var r = new Radix(10);
        var ex = assertThrows(IllegalArgumentException.class, () -> r.convertDecimalToAnother(1));
        assertEquals("Invalid Radix", ex.getMessage());
    }

    @Test
    void convert_base2() {
        assertEquals("1010", new Radix(10).convertDecimalToAnother(2));
    }

    @Test
    void convert_base16() {
        assertEquals("1F", new Radix(31).convertDecimalToAnother(16));
        assertEquals("FF", new Radix(255).convertDecimalToAnother(16));
        assertEquals("A", new Radix(10).convertDecimalToAnother(16));
    }
}
