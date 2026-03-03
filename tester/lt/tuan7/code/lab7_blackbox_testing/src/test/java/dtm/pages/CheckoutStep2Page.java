package dtm.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CheckoutStep2Page {
    private final WebDriver driver;

    @FindBy(css = ".summary_subtotal_label")
    private WebElement itemTotalLabel; // "Item total: $29.99"

    @FindBy(css = ".summary_tax_label")
    private WebElement taxLabel;       // "Tax: $2.40"

    @FindBy(css = ".summary_total_label")
    private WebElement totalLabel;     // "Total: $32.39"

    @FindBy(id = "finish")
    private WebElement finishBtn;

    @FindBy(id = "cancel")
    private WebElement cancelBtn;

    public CheckoutStep2Page(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public double getItemTotal() {
        return parseMoney(itemTotalLabel.getText());
    }

    public double getTax() {
        return parseMoney(taxLabel.getText());
    }

    public double getTotal() {
        return parseMoney(totalLabel.getText());
    }

    public void finish() {
        finishBtn.click();
    }

    public void cancel() {
        cancelBtn.click();
    }

    private double parseMoney(String labelText) {
        // lấy phần sau dấu $
        int idx = labelText.indexOf("$");
        if (idx < 0) throw new IllegalArgumentException("Không parse được tiền: " + labelText);
        return Double.parseDouble(labelText.substring(idx + 1).trim());
    }
}