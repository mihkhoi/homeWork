package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

public
class UiBaseTest {
  protected
    WebDriver driver;

    @BeforeMethod(alwaysRun = true) public void setupDriver() {
        ChromeOptions options = new ChromeOptions();
        String headless = System.getenv().getOrDefault("HEADLESS", "false");
        if ("true".equalsIgnoreCase(headless)) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--window-size=1600,1000");
        options.addArguments("--disable-notifications");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    @AfterMethod(alwaysRun = true) public void tearDownDriver() {
        if (driver != null) {
            driver.quit();
        }
    }
}
