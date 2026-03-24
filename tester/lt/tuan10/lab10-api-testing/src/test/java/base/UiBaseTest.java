package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

public
class UiBaseTest {
  protected
    WebDriver driver;

    @BeforeMethod(alwaysRun = true) public void setupDriver() {
        EdgeOptions options = new EdgeOptions();

        // Muốn chạy ẩn thì mở dòng dưới
        // options.addArguments("--headless=new");

        options.addArguments("--window-size=1600,1000");
        options.addArguments("--disable-notifications");

        driver = new EdgeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    @AfterMethod(alwaysRun = true) public void tearDownDriver() {
        if (driver != null) {
            driver.quit();
        }
    }
}
