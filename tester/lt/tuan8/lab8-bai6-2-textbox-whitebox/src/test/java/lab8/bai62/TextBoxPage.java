package lab8.bai62;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public
class TextBoxPage {

  private
    final WebDriver driver;
  private
    final WebDriverWait wait;

    @FindBy(id = "userName") private WebElement nameField;

    @FindBy(id = "userEmail") private WebElement emailField;

    @FindBy(id = "currentAddress") private WebElement currentAddressField;

    @FindBy(id = "permanentAddress") private WebElement permanentAddressField;

    @FindBy(id = "submit") private WebElement submitBtn;

    @FindBy(id = "output") private WebElement outputSection;

  public
    TextBoxPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

  public
    void fillForm(String name, String email, String currentAddress, String permanentAddress) {
        wait.until(ExpectedConditions.visibilityOf(nameField)).clear();
        nameField.sendKeys(name);

        emailField.clear();
        emailField.sendKeys(email);

        currentAddressField.clear();
        currentAddressField.sendKeys(currentAddress);

        permanentAddressField.clear();
        permanentAddressField.sendKeys(permanentAddress);
    }

  public
    void submit() {
        ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", submitBtn);
        ((JavascriptExecutor)driver).executeScript("arguments[0].click();", submitBtn);
    }

  public
    void fillAndSubmit(String name, String email, String currentAddress, String permanentAddress) {
        fillForm(name, email, currentAddress, permanentAddress);
        submit();
    }

  public
    boolean isOutputDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(outputSection)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

  public
    String getOutputText() {
        if (!isOutputDisplayed()) {
            return "";
        }
        return outputSection.getText();
    }

  public
    boolean isEmailFieldValidByBrowser() {
        return (Boolean)((JavascriptExecutor)driver)
            .executeScript("return arguments[0].checkValidity();", emailField);
    }

  public
    String getNameValue() {
        return nameField.getAttribute("value");
    }

  public
    String getEmailValue() {
        return emailField.getAttribute("value");
    }

  public
    String getCurrentAddressValue() {
        return currentAddressField.getAttribute("value");
    }

  public
    String getPermanentAddressValue() {
        return permanentAddressField.getAttribute("value");
    }
}
