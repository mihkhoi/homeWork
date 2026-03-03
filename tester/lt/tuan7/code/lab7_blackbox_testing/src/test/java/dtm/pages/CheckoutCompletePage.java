package dtm.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CheckoutCompletePage {
    private final WebDriver driver;

    @FindBy(css = ".complete-header")
    private WebElement header; // "Thank you for your order!"

    @FindBy(id = "back-to-products")
    private WebElement backHomeBtn;

    public CheckoutCompletePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public String getHeader() {
        return header.getText().trim();
    }

    public void backHome() {
        backHomeBtn.click();
    }
}