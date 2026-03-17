package framework.pages;

import framework.base.BasePage;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CartPage extends BasePage {

    @FindBy(css = ".cart_item")
    private List<WebElement> cartItems;

    @FindBy(css = ".cart_item .inventory_item_name")
    private List<WebElement> itemNames;

    @FindBy(css = ".cart_button")
    private List<WebElement> removeButtons;

    @FindBy(id = "checkout")
    private WebElement checkoutButton;

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public int getItemCount() {
        try {
            return cartItems.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public CartPage removeFirstItem() {
        if (removeButtons == null || removeButtons.isEmpty()) {
            return this;
        }
        waitAndClick(removeButtons.get(0));
        return this;
    }

    public CheckoutPage goToCheckout() {
        waitAndClick(checkoutButton);
        waitForPageLoad();
        return new CheckoutPage(driver);
    }

    public List<String> getItemNames() {
        List<String> names = new ArrayList<>();
        try {
            for (WebElement itemName : itemNames) {
                names.add(itemName.getText().trim());
            }
        } catch (Exception e) {
            return List.of();
        }
        return names;
    }

    public boolean isLoaded() {
        return isElementVisible(By.cssSelector(".cart_list"));
    }
}
