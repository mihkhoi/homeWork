package dtm.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.List;

public class CartPage {
    private final WebDriver driver;

    @FindBy(css = ".cart_item")
    private List<WebElement> cartItems;

    @FindBy(id = "checkout")
    private WebElement checkoutBtn;

    @FindBy(id = "continue-shopping")
    private WebElement continueShoppingBtn;

    public CartPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public int demSoItemTrongGio() {
        return cartItems == null ? 0 : cartItems.size();
    }

    public List<String> layTenItems() {
        List<String> names = new ArrayList<>();
        for (WebElement item : cartItems) {
            names.add(item.findElement(By.cssSelector(".inventory_item_name")).getText().trim());
        }
        return names;
    }

    public void removeItemTheoTen(String ten) {
        for (WebElement item : cartItems) {
            String name = item.findElement(By.cssSelector(".inventory_item_name")).getText().trim();
            if (name.equalsIgnoreCase(ten.trim())) {
                item.findElement(By.cssSelector("button.cart_button")).click(); // Remove
                return;
            }
        }
        throw new NoSuchElementException("Không thấy item trong giỏ: " + ten);
    }

    public void removeHet() {
        // remove theo vòng lặp vì list thay đổi
        while (demSoItemTrongGio() > 0) {
            cartItems.get(0).findElement(By.cssSelector("button.cart_button")).click();
            // refresh lại page object state bằng cách tìm lại DOM
            cartItems = driver.findElements(By.cssSelector(".cart_item"));
        }
    }

    public void checkout() {
        checkoutBtn.click();
    }

    public void continueShopping() {
        continueShoppingBtn.click();
    }
}