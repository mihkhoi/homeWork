package framework.factory;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeDriverService;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.Duration;

public final class DriverFactory {

    private DriverFactory() {
    }

    public static WebDriver createDriver(String browser) {
        boolean isCI = System.getenv("CI") != null;
        // Ưu tiên system property, sau đó environment variable
        String gridUrl = System.getProperty("grid.url");
        if (gridUrl == null || gridUrl.isBlank()) {
            gridUrl = System.getenv("GRID_URL");
        }

        if (browser == null || browser.isBlank()) {
            browser = "edge";
        }

        // Nếu có grid.url, sử dụng RemoteWebDriver
        if (gridUrl != null && !gridUrl.isBlank()) {
            return createRemoteDriver(browser, gridUrl);
        }

        // Ngược lại, chạy local
        return switch (browser.toLowerCase()) {
            case "chrome" -> createChromeDriver(isCI);
            case "firefox" -> createFirefoxDriver(isCI);
            case "edge" -> createEdgeDriver(isCI);
            default -> throw new IllegalArgumentException("Browser '" + browser + "' không được hỗ trợ");
        };
    }

    // ============== LOCAL DRIVERS ==============

    private static WebDriver createChromeDriver(boolean headless) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();

        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--window-size=1920,1080");
            System.out.println("[DriverFactory] Chạy Chrome HEADLESS (CI mode)");
        } else {
            options.addArguments("--start-maximized");
            System.out.println("[DriverFactory] Chạy Chrome normal (Local mode)");
        }

        return new ChromeDriver(options);
    }

    private static WebDriver createFirefoxDriver(boolean headless) {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();

        if (headless) {
            options.addArguments("-headless");
            options.addArguments("--width=1920");
            options.addArguments("--height=1080");
            System.out.println("[DriverFactory] Chạy Firefox HEADLESS (CI mode)");
        } else {
            System.out.println("[DriverFactory] Chạy Firefox normal (Local mode)");
        }

        return new FirefoxDriver(options);
    }

    private static WebDriver createEdgeDriver(boolean headless) {
        // Thử dùng WebDriverManager trước (cho CI environment đủ quyền download)
        try {
            WebDriverManager.edgedriver().setup();
            EdgeOptions options = new EdgeOptions();

            if (headless) {
                options.addArguments("--headless=new");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--window-size=1920,1080");
                System.out.println("[DriverFactory] Chạy Edge HEADLESS (CI mode)");
            } else {
                options.addArguments("--start-maximized");
                System.out.println("[DriverFactory] Chạy Edge normal (Local mode)");
            }

            return new EdgeDriver(options);
        } catch (Exception e) {
            // Nếu WebDriverManager fail, dùng driver local
            System.out.println("[DriverFactory] WebDriverManager fail, dùng local driver: " + e.getMessage());
            return createEdgeDriverLocal(headless);
        }
    }

    private static WebDriver createEdgeDriverLocal(boolean headless) {
        String driverPath = Paths.get("drivers", "msedgedriver.exe").toAbsolutePath().toString();
        
        EdgeDriverService service = new EdgeDriverService.Builder()
                .usingDriverExecutable(new File(driverPath))
                .build();

        EdgeOptions options = new EdgeOptions();

        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--window-size=1920,1080");
        } else {
            options.addArguments("--start-maximized");
        }

        return new EdgeDriver(service, options);
    }

    // ============== REMOTE DRIVER FOR GRID ==============

    private static WebDriver createRemoteDriver(String browser, String gridUrl) {
        try {
            URL gridEndpoint = new URL(gridUrl + "/wd/hub");
            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setBrowserName(browser.toLowerCase());

            // Thêm browser-specific options
            if (browser.equalsIgnoreCase("chrome")) {
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--no-sandbox", "--disable-dev-shm-usage");
                caps.merge(options);
            } else if (browser.equalsIgnoreCase("firefox")) {
                FirefoxOptions options = new FirefoxOptions();
                caps.merge(options);
            } else if (browser.equalsIgnoreCase("edge")) {
                EdgeOptions options = new EdgeOptions();
                options.addArguments("--no-sandbox", "--disable-dev-shm-usage");
                caps.merge(options);
            }

            RemoteWebDriver driver = new RemoteWebDriver(gridEndpoint, caps);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            
            System.out.println("[DriverFactory] Grid Session tạo thành công");
            System.out.println("  Grid URL: " + gridUrl);
            System.out.println("  Browser: " + browser);
            System.out.println("  Session ID: " + driver.getSessionId());
            
            return driver;
        } catch (MalformedURLException e) {
            throw new RuntimeException("Grid URL không hợp lệ: " + gridUrl, e);
        } catch (Exception e) {
            throw new RuntimeException("Không thể tạo Grid session: " + e.getMessage(), e);
        }
    }
}