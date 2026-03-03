package dtm.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CheckoutStep1Page {
    private final WebDriver driver;

    @FindBy(id = "first-name")
    private WebElement firstName;

    @FindBy(id = "last-name")
    private WebElement lastName;

    @FindBy(id = "postal-code")
    private WebElement zipCode;

    @FindBy(id = "continue")
    private WebElement continueBtn;

    @FindBy(id = "cancel")
    private WebElement cancelBtn;

    @FindBy(css = "h3[data-test='error']")
    private java.util.List<WebElement> errors; // list để tránh crash khi không có error

    public CheckoutStep1Page(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void nhapFirstName(String v) {
        firstName.clear();
        if (v != null) firstName.sendKeys(v);
    }

    public void nhapLastName(String v) {
        lastName.clear();
        if (v != null) lastName.sendKeys(v);
    }

    public void nhapZip(String v) {
        zipCode.clear();
        if (v != null) zipCode.sendKeys(v);
    }

    public void continueCheckout() {
        continueBtn.click();
    }

    public void cancel() {
        cancelBtn.click();
    }

    public String layThongBaoLoi() {
        if (errors == null || errors.isEmpty()) return null;
        return errors.get(0).getText().trim();
    }
}