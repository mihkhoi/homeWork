package fpoly;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import static org.junit.Assert.*;

public class ErrorCollectorExampleTest {

    @Rule
    public ErrorCollector collector = new ErrorCollector();

    @Test
    public void example_collectMultipleErrors() {
        collector.addError(new Throwable("There is an error in first line"));
        collector.addError(new Throwable("There is an error in second line"));

        try {
            assertEquals("A", "B"); // cố tình sai
        } catch (Throwable t) {
            collector.addError(t);
        }

        System.out.println("Hello - vẫn chạy tiếp");
    }
}
