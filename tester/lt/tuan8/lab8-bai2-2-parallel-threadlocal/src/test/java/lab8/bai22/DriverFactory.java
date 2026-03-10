package lab8.bai22;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

public
class DriverFactory {

  private
    static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();

  public
    static void initDriver(String browser) {
        WebDriver driver;

        switch (browser.toLowerCase()) {
        case "edge":
        default:
            System.setProperty("webdriver.edge.driver", "drivers\\msedgedriver.exe");

            EdgeOptions options = new EdgeOptions();
            // Neu can chi dinh Edge thu cong thi mo comment dong duoi
            // options.setBinary("C:\\Program Files (x86)\\Microsoft\\Edge\\Application\\msedge.exe");

            driver = new EdgeDriver(options);
            break;
        }

        driver.manage().window().maximize();
        tlDriver.set(driver);
    }

  public
    static WebDriver getDriver() {
        return tlDriver.get();
    }

  public
    static void quitDriver() {
        if (tlDriver.get() != null) {
            tlDriver.get().quit();
            tlDriver.remove();
        }
    }
}
