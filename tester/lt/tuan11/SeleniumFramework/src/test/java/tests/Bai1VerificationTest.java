package tests;

import framework.base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

public class Bai1VerificationTest extends BaseTest {

    @Test(enabled = false)
    @Feature("Kiểm thử cơ bản")
    @Story("Chụp ảnh khi fail")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test cố tình fail để kiểm tra screenshot")
    public void testFailIntentionalToCaptureScreenshot() {
        String title = getDriver().getTitle();
        Assert.assertTrue(title.contains("ABCXYZ"),
                "Fail co y de kiem tra screenshot");
    }

    @Test
    @Feature("Kiểm thử UI")
    @Story("Xác minh trang web")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test song song 1 - Kiểm tra URL trang Saucedemo")
    public void testParallelOne() {
        Assert.assertTrue(getDriver().getCurrentUrl().contains("saucedemo"));
    }

    @Test
    @Feature("Kiểm thử UI")
    @Story("Xác minh trang web")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test song song 2 - Kiểm tra title không rỗng")
    public void testParallelTwo() {
        Assert.assertTrue(getDriver().getTitle().length() > 0);
    }

    @Test
    @Feature("Kiểm thử UI")
    @Story("Quản lý cửa sổ")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test song song 3 - Kiểm tra số cửa sổ")
    public void testParallelThree() {
        Assert.assertEquals(getDriver().getWindowHandles().size(), 1);
    }
}
