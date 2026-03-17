package framework.base;

import framework.config.ConfigReader;
import framework.factory.DriverFactory;
import framework.utils.ScreenshotUtil;
import java.time.Duration;
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

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        try {
            if (getDriver() != null && result.getStatus() == ITestResult.FAILURE) {
                ScreenshotUtil.capture(getDriver(), result.getName());
            }
        } finally {
            if (getDriver() != null) {
                getDriver().quit();
                TL_DRIVER.remove();
            }
        }
    }
}
