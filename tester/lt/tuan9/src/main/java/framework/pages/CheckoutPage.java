package framework.pages;

import framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutPage extends BasePage {

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return isElementVisible(By.id("first-name"))
                || isElementVisible(By.cssSelector(".checkout_info"))
                || isElementVisible(By.cssSelector(".checkout_container"));
    }
}
