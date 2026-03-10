package lab8.bai22;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.time.Duration;

public
class CartParallelTest extends BaseTest {

    @Parameters({"baseUrl"})
        @Test(description = "Them vao gio song song - CartTest") public void testAddToCartParallel(String baseUrl) throws InterruptedException {
        DriverFactory.getDriver().get(baseUrl);

        WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(10));

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name"))).sendKeys("standard_user");
        DriverFactory.getDriver().findElement(By.id("password")).sendKeys("secret_sauce");
        DriverFactory.getDriver().findElement(By.id("login-button")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-to-cart-sauce-labs-backpack"))).click();

        String badgeText = wait.until(
                                   ExpectedConditions.visibilityOfElementLocated(By.className("shopping_cart_badge")))
                               .getText();

        Assert.assertEquals(
            badgeText,
            "1",
            "Them san pham vao gio hang that bai");

        System.out.println("[" + Thread.currentThread().getId() + "] CartParallelTest dang chay");
        Thread.sleep(5000);
    }
}
