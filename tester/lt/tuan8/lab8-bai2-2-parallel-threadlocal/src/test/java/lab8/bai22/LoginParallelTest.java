package lab8.bai22;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.time.Duration;

public
class LoginParallelTest extends BaseTest {

    @Parameters({"baseUrl"})
        @Test(description = "Dang nhap song song - LoginTest") public void testLoginSuccessParallel(String baseUrl) throws InterruptedException {
        DriverFactory.getDriver().get(baseUrl);

        WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(10));

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name"))).sendKeys("standard_user");
        DriverFactory.getDriver().findElement(By.id("password")).sendKeys("secret_sauce");
        DriverFactory.getDriver().findElement(By.id("login-button")).click();

        wait.until(ExpectedConditions.urlContains("inventory.html"));

        Assert.assertTrue(
            DriverFactory.getDriver().getCurrentUrl().contains("inventory.html"),
            "Dang nhap song song that bai");

        System.out.println("[" + Thread.currentThread().getId() + "] LoginParallelTest dang chay");
        Thread.sleep(5000);
    }
}
