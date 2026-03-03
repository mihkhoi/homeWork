package dtm.base;

import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public abstract class BaseTest {

    private static final ThreadLocal<WebDriver> TL_DRIVER = new ThreadLocal<>();

    public WebDriver getDriver() {
        return TL_DRIVER.get();
    }

    @BeforeMethod
    public void setUp(Method method) {
        // ✅ OFFLINE: trỏ thẳng tới driver trong folder drivers/
        System.setProperty("webdriver.edge.driver", "drivers/msedgedriver.exe");

        WebDriver driver = new EdgeDriver();
        TL_DRIVER.set(driver);

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        System.out.println("=== START TEST: " + method.getName() + " ===");
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        try {
            if (result.getStatus() == ITestResult.FAILURE) {
                takeScreenshot(result.getName());
            }
        } finally {
            WebDriver driver = getDriver();
            if (driver != null) driver.quit();
            TL_DRIVER.remove();
        }
    }

    private void takeScreenshot(String testName) {
        WebDriver driver = getDriver();
        if (driver == null) return;

        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

        Path folder = Paths.get("screenshots");
        Path dest = folder.resolve(testName + "_" + ts + ".png");

        try {
            Files.createDirectories(folder);
            Files.copy(src.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("[SCREENSHOT] " + dest.toAbsolutePath());
        } catch (IOException e) {
            System.out.println("[SCREENSHOT ERROR] " + e.getMessage());
        }
    }
}