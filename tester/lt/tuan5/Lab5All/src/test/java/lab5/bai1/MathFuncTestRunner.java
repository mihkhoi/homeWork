
package lab5.bai1;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public
class MathFuncTestRunner {
  public
    static void main(String[] args) {
        Result result = JUnitCore.runClasses(MathFuncTest.class);

        for (Failure f : result.getFailures()) {
            System.out.println(f.toString());
        }

        System.out.println("Run: " + result.getRunCount() + " | Failed: " + result.getFailureCount() + " | Ignored: " + result.getIgnoreCount() + " | Success: " + result.wasSuccessful());
    }
}
