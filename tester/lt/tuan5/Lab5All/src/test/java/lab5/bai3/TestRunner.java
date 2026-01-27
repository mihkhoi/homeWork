
package lab5.bai3;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public
class TestRunner {
  public
    static void main(String[] args) {
        Result result = JUnitCore.runClasses(JunitAnnotationsExample.class);

        for (Failure f : result.getFailures()) {
            System.out.println(f.toString());
        }

        System.out.println("Run: " + result.getRunCount() + " | Failed: " + result.getFailureCount() + " | Ignored: " + result.getIgnoreCount() + " | Success: " + result.wasSuccessful());
    }
}
