package dtm.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class LoginPage {

    private final WebDriver driver;

    @FindBy(id = "user-name")
    private WebElement userNameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    @FindBy(css = "div[data-test='error']")
    private List<WebElement> errorMessage; // list để tránh crash khi không có error

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void dangNhap(String user, String pass) {
        userNameField.clear();
        if (user != null) userNameField.sendKeys(user);

        passwordField.clear();
        if (pass != null) passwordField.sendKeys(pass);

        loginButton.click();
    }

    public boolean isDangOTrangSanPham() {
        return driver.getCurrentUrl().contains("/inventory.html");
    }

    public String layThongBaoLoi() {
        if (errorMessage == null || errorMessage.isEmpty()) return null;
        return errorMessage.get(0).getText().trim();
    }
}