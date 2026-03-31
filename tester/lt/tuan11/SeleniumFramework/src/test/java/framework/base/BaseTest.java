package framework.base;

import framework.config.ConfigReader;
import framework.factory.DriverFactory;
import framework.utils.ScreenshotUtil;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.allure.model.Status;
import java.io.ByteArrayInputStream;
import java.time.Duration;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public abstract class BaseTest {

    private static final ThreadLocal<WebDriver> TL_DRIVER = new ThreadLocal<>();

    protected WebDriver getDriver() {
        return TL_DRIVER.get();
    }

    @Step("Mở trình duyệt và điều hướng đến ứng dụng")
    @BeforeMethod(alwaysRun = true)
    @Parameters({"browser", "env"})
    public void setUp(@Optional("edge") String browser,
                      @Optional("dev") String env) {

        System.setProperty("env", env);
        ConfigReader.reset();

        WebDriver driver = DriverFactory.createDriver(browser);
        TL_DRIVER.set(driver);

        getDriver().manage().window().maximize();
        getDriver().manage().timeouts().implicitlyWait(
                Duration.ofSeconds(ConfigReader.getInstance().getImplicitWait())
        );
        getDriver().get(ConfigReader.getInstance().getBaseUrl());
    }

    @Step("Đóng trình duyệt")
    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        try {
            if (getDriver() != null) {
                if (result.getStatus() == ITestResult.FAILURE) {
                    ScreenshotUtil.capture(getDriver(), result.getName());
                    attachScreenshotToAllure();
                }
            }
        } finally {
            if (getDriver() != null) {
                getDriver().quit();
                TL_DRIVER.remove();
            }
        }
    }

    private void attachScreenshotToAllure() {
        try {
            byte[] screenshot = ((TakesScreenshot) getDriver())
                    .getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment("Screenshot on Failure",
                    "image/png",
                    new ByteArrayInputStream(screenshot),
                    ".png");
        } catch (Exception e) {
            System.out.println("Failed to attach screenshot: " + e.getMessage());
        }
    }
}
