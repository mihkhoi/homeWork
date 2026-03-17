package framework.pages;

import framework.base.BasePage;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class InventoryPage extends BasePage {

    @FindBy(css = ".inventory_list")
    private WebElement inventoryList;

    @FindBy(css = ".shopping_cart_badge")
    private WebElement cartBadge;

    @FindBy(css = ".shopping_cart_link")
    private WebElement cartLink;

    @FindBy(css = ".inventory_item")
    private List<WebElement> inventoryItems;

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return isElementVisible(By.cssSelector(".inventory_list"));
    }

    public InventoryPage addFirstItemToCart() {
        if (inventoryItems.isEmpty()) {
            throw new IllegalStateException("Khong tim thay san pham nao tren InventoryPage");
        }

        WebElement firstButton = inventoryItems.get(0).findElement(By.cssSelector("button"));
        waitAndClick(firstButton);
        return this;
    }

    public InventoryPage addItemByName(String name) {
        for (WebElement item : inventoryItems) {
            WebElement itemName = item.findElement(By.cssSelector(".inventory_item_name"));
            if (itemName.getText().trim().equalsIgnoreCase(name.trim())) {
                WebElement addButton = item.findElement(By.cssSelector("button"));
                scrollToElement(addButton);
                waitAndClick(addButton);
                return this;
            }
        }
        throw new IllegalArgumentException("Khong tim thay san pham: " + name);
    }

    public int getCartItemCount() {
        try {
            return Integer.parseInt(cartBadge.getText().trim());
        } catch (Exception e) {
            return 0;
        }
    }

    public CartPage goToCart() {
        waitAndClick(cartLink);
        waitForPageLoad();
        return new CartPage(driver);
    }
}
