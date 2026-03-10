package lab8.bai62;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public
class BaseTest {

  protected
    WebDriver driver;
  protected
    final String baseUrl = "https://demoqa.com/text-box";

    @BeforeMethod public void setUp() {
        System.setProperty("webdriver.edge.driver", "drivers\\msedgedriver.exe");

        EdgeOptions options = new EdgeOptions();
        // Neu can thi mo comment dong duoi va sua dung path Edge
        // options.setBinary("C:\\Program Files (x86)\\Microsoft\\Edge\\Application\\msedge.exe");

        driver = new EdgeDriver(options);
        driver.manage().window().maximize();
        driver.get(baseUrl);
    }

    @AfterMethod public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
