package framework.utils;

import framework.config.ConfigReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public final class ScreenshotUtil {

    private ScreenshotUtil() {
    }

    public static String capture(WebDriver driver, String testName) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());
        String folderPath = ConfigReader.getInstance().getScreenshotPath();
        String fileName = testName + "_" + timestamp + ".png";

        try {
            Files.createDirectories(Path.of(folderPath));
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Path targetPath = Path.of(folderPath, fileName);
            Files.copy(srcFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            return targetPath.toString();
        } catch (IOException e) {
            throw new RuntimeException("Khong the chup screenshot", e);
        }
    }
}
