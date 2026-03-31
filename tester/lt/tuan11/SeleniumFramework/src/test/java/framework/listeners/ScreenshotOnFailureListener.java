package framework.listeners;

import framework.utils.ScreenshotUtil;
import io.qameta.allure.Allure;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Listener để tự động chụp ảnh khi test fail và đính kèm vào Allure Report
 */
public class ScreenshotOnFailureListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        // Lấy driver từ test object nếu có
        Object testObject = result.getInstance();
        WebDriver driver = null;

        try {
            // Try to get driver from test class
            if (testObject != null && testObject.getClass().getMethod("getDriver") != null) {
                java.lang.reflect.Method method = testObject.getClass().getMethod("getDriver");
                driver = (WebDriver) method.invoke(testObject);
            }
        } catch (Exception e) {
            // Nếu không lấy được, skip
        }

        if (driver != null) {
            try {
                // Chụp screenshot
                String screenshotPath = ScreenshotUtil.capture(driver, result.getName());
                
                // Đính kèm vào Allure Report
                if (screenshotPath != null) {
                    Path path = Path.of(screenshotPath);
                    if (Files.exists(path)) {
                        byte[] screenshotBytes = Files.readAllBytes(path);
                        Allure.addAttachment(
                            "Screenshot on Failure - " + result.getName(),
                            "image/png",
                            java.io.ByteArrayInputStream.nullInputStream().toString(),
                            "png"
                        );
                        // Hoặc dùng cách này:
                        Allure.addAttachment(
                            "Failed Test Screenshot",
                            "image/png",
                            new java.io.ByteArrayInputStream(screenshotBytes),
                            "png"
                        );
                    }
                }
            } catch (Exception e) {
                System.out.println("Lỗi khi chụp screenshot: " + e.getMessage());
            }
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        Allure.feature(result.getTestClass().getRealClass().getSimpleName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        // Test pass - không cần làm gì
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        // Test skip
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // Test fail nhưng trong success percentage
    }

    @Override
    public void onStart(org.testng.ITestContext context) {
        // Test suite start
    }

    @Override
    public void onFinish(org.testng.ITestContext context) {
        // Test suite finish
    }
}
