package framework.factory;

import framework.config.ConfigReader;
import java.nio.file.Files;
import java.nio.file.Path;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

public final class DriverFactory {

    private DriverFactory() {
    }

    public static WebDriver createDriver(String browser) {
        if (browser == null || browser.isBlank()) {
            browser = "edge";
        }

        if (!"edge".equalsIgnoreCase(browser)) {
            throw new IllegalArgumentException("Ban dang dung ban local driver, hien chi ho tro edge.");
        }

        String driverPath = Path.of(ConfigReader.getInstance().getEdgeDriverPath())
                .toAbsolutePath()
                .toString();

        if (!Files.exists(Path.of(driverPath))) {
            throw new RuntimeException(
                    "Khong tim thay msedgedriver.exe tai: " + driverPath
                            + " . Hay copy file vao folder drivers."
            );
        }

        System.setProperty("webdriver.edge.driver", driverPath);

        EdgeOptions edgeOptions = new EdgeOptions();
        return new EdgeDriver(edgeOptions);
    }
}
