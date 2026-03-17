package tests;

import framework.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class Bai1VerificationTest extends BaseTest {

    @Test(description = "Fail co y de kiem tra screenshot")
    public void testFailIntentionalToCaptureScreenshot() {
        String title = getDriver().getTitle();
        Assert.assertTrue(title.contains("ABCXYZ"),
                "Fail co y de kiem tra screenshot");
    }

    @Test(description = "Parallel test 1")
    public void testParallelOne() {
        Assert.assertTrue(getDriver().getCurrentUrl().contains("saucedemo"));
    }

    @Test(description = "Parallel test 2")
    public void testParallelTwo() {
        Assert.assertTrue(getDriver().getTitle().length() > 0);
    }

    @Test(description = "Parallel test 3")
    public void testParallelThree() {
        Assert.assertEquals(getDriver().getWindowHandles().size(), 1);
    }
}
