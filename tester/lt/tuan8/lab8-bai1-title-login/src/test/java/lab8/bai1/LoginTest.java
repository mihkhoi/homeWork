package lab8.bai1;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

public
class LoginTest {

    WebDriver driver;
    WebDriverWait wait;
    String baseUrl = "https://www.saucedemo.com/";

    By txtUsername = By.id("user-name");
    By txtPassword = By.id("password");
    By btnLogin = By.id("login-button");
    By errorMsg = By.cssSelector("h3[data-test='error']");

    @BeforeMethod public void setUp() {
        System.setProperty("webdriver.edge.driver", "drivers\\msedgedriver.exe");

        EdgeOptions options = new EdgeOptions();
        options.setBinary("C:\\Program Files (x86)\\Microsoft\\Edge\\Application\\msedge.exe");
        // Neu may ban la:
        // C:\\Program Files\\Microsoft\\Edge\\Application\\msedge.exe
        // thi doi lai dong tren

        driver = new EdgeDriver(options);
        driver.manage().window().maximize();
        driver.get(baseUrl);

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(txtUsername));
    }

  public
    void login(String username, String password) {
        WebElement usernameElement =
            wait.until(ExpectedConditions.visibilityOfElementLocated(txtUsername));
        usernameElement.clear();
        usernameElement.sendKeys(username);

        WebElement passwordElement =
            wait.until(ExpectedConditions.visibilityOfElementLocated(txtPassword));
        passwordElement.clear();
        passwordElement.sendKeys(password);

        WebElement loginButton =
            wait.until(ExpectedConditions.elementToBeClickable(btnLogin));
        loginButton.click();
    }

  public
    String getErrorText() {
        return wait.until(
                       ExpectedConditions.visibilityOfElementLocated(errorMsg))
            .getText();
    }

  public
    void takeScreenshot(String testName) throws IOException {
        File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        File dir = new File("screenshots");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File dest = new File(dir, testName + ".png");
        FileUtils.copyFile(src, dest);
    }

    @Test(description = "Dang nhap thanh cong voi tai khoan hop le") public void testLoginSuccess() {
        login("standard_user", "secret_sauce");

        wait.until(ExpectedConditions.urlContains("inventory.html"));
        String currentUrl = driver.getCurrentUrl();

        Assert.assertTrue(
            currentUrl.contains("inventory.html"),
            "Dang nhap hop le nhung khong chuyen sang inventory.html");
    }

    @Test(description = "Dang nhap sai mat khau") public void testLoginWrongPassword() {
        login("standard_user", "wrong_password");

        String expected = "Epic sadface: Username and password do not match any user in this service";
        String actual = getErrorText();

        Assert.assertEquals(
            actual,
            expected,
            "Thong bao loi khi nhap sai mat khau khong dung!");
    }

    @Test(description = "Bo trong username") public void testLoginEmptyUsername() {
        login("", "secret_sauce");

        String expected = "Epic sadface: Username is required";
        String actual = getErrorText();

        Assert.assertEquals(
            actual,
            expected,
            "Thong bao loi khi bo trong username khong dung!");
    }

    @Test(description = "Bo trong password") public void testLoginEmptyPassword() {
        login("standard_user", "");

        String expected = "Epic sadface: Password is required";
        String actual = getErrorText();

        Assert.assertEquals(
            actual,
            expected,
            "Thong bao loi khi bo trong password khong dung!");
    }

    @Test(description = "Dang nhap bang locked_out_user") public void testLoginLockedUser() {
        login("locked_out_user", "secret_sauce");

        String expected = "Epic sadface: Sorry, this user has been locked out.";
        String actual = getErrorText();

        Assert.assertEquals(
            actual,
            expected,
            "Thong bao loi locked user khong dung!");
    }

    @AfterMethod public void tearDown(ITestResult result) throws IOException {
        if (driver != null) {
            takeScreenshot(result.getName());
            driver.quit();
        }
    }
}
