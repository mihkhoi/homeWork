package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

public
class InventoryPage {
  private
    final WebDriver driver;

  private
    final By title = By.className("title");
  private
    final By cartBadge = By.className("shopping_cart_badge");
  private
    final By addToCartButtons = By.cssSelector("button.btn_inventory");
  private
    final By cartLink = By.className("shopping_cart_link");

  public
    InventoryPage(WebDriver driver) {
        this.driver = driver;
    }

  public
    boolean isLoaded() {
        return driver.findElement(title).getText().equals("Products");
    }

  public
    void addFirstNItems(int n) {
        List<org.openqa.selenium.WebElement> buttons = driver.findElements(addToCartButtons);
        for (int i = 0; i < n && i < buttons.size(); i++) {
            buttons.get(i).click();
        }
    }

  public
    int getCartItemCount() {
        List<org.openqa.selenium.WebElement> badges = driver.findElements(cartBadge);
        if (badges.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(badges.get(0).getText().trim());
    }

  public
    CartPage goToCart() {
        driver.findElement(cartLink).click();
        return new CartPage(driver);
    }
}
