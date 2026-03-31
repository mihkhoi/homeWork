package framework.base;

import framework.config.ConfigReader;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Lop cha cho tat ca Page Object.
 * Chua cac method Selenium dung chung trong framework.
 */
public abstract class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(ConfigReader.getInstance().getExplicitWait())
        );
        PageFactory.initElements(driver, this);
    }

    /**
     * Cho element click duoc roi moi click.
     *
     * @param element phan tu can click
     */
    protected void waitAndClick(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    /**
     * Cho element hien thi roi clear va nhap text.
     *
     * @param element phan tu can nhap
     * @param text du lieu can nhap
     */
    protected void waitAndType(WebElement element, String text) {
        wait.until(ExpectedConditions.visibilityOf(element));
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Cho element hien thi roi lay text.
     *
     * @param element phan tu can lay text
     * @return noi dung text
     */
    protected String getText(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element)).getText().trim();
    }

    /**
     * Kiem tra element co hien thi hay khong.
     * Co xu ly stale element theo yeu cau bai.
     *
     * @param locator locator cua phan tu
     * @return true neu hien thi, nguoc lai false
     */
    protected boolean isElementVisible(By locator) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException | TimeoutException e) {
            return false;
        }
    }

    /**
     * Cuon toi element bang JavaScript.
     *
     * @param element phan tu can cuon toi
     */
    protected void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
    }

    /**
     * Cho trang load xong theo document.readyState.
     */
    protected void waitForPageLoad() {
        wait.until(webDriver ->
                ((JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState")
                        .equals("complete")
        );
    }

    /**
     * Lay gia tri attribute cua element.
     *
     * @param element phan tu can lay attribute
     * @param attributeName ten attribute
     * @return gia tri attribute
     */
    protected String getAttribute(WebElement element, String attributeName) {
        return wait.until(ExpectedConditions.visibilityOf(element)).getAttribute(attributeName);
    }
}
