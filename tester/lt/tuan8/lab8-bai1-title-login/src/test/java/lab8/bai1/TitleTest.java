package lab8.bai1;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public
class TitleTest {

    WebDriver driver;
    String baseUrl = "https://www.saucedemo.com/";

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
    }

    @Test(description = "Kiem thu tieu de trang chu") public void testTitle() {
        String expectedTitle = "Swag Labs";
        String actualTitle = driver.getTitle();

        Assert.assertEquals(
            actualTitle,
            expectedTitle,
            "Tieu de trang khong dung!");
    }

    @Test(description = "Kiem thu URL trang chu") public void testURL() {
        String actualUrl = driver.getCurrentUrl();

        Assert.assertTrue(
            actualUrl.contains("saucedemo"),
            "URL khong hop le!");
    }

    @Test(description = "Kiem thu page source co chua chuoi Swag Labs") public void testPageSource() {
        String source = driver.getPageSource();

        Assert.assertTrue(
            source.contains("Swag Labs"),
            "Page source khong chua noi dung mong doi!");
    }

    @Test(description = "Kiem thu form dang nhap hien thi") public void testLoginFormDisplayed() {
        WebElement username = driver.findElement(By.id("user-name"));
        WebElement password = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("login-button"));

        Assert.assertTrue(username.isDisplayed(), "O username khong hien thi!");
        Assert.assertTrue(password.isDisplayed(), "O password khong hien thi!");
        Assert.assertTrue(loginButton.isDisplayed(), "Nut login khong hien thi!");
    }

    @AfterMethod public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
