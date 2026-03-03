package dtm.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;

public class InventoryPage {
    private final WebDriver driver;

    @FindBy(css = "select[data-test='product_sort_container']")
    private WebElement sortDropdown;

    @FindBy(css = ".inventory_item")
    private List<WebElement> inventoryItems;

    @FindBy(css = ".shopping_cart_link")
    private WebElement cartLink;

    @FindBy(css = ".shopping_cart_badge")
    private List<WebElement> cartBadges; // list để tránh NoSuchElement khi badge không tồn tại

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    /** Thêm sản phẩm theo tên hiển thị (ví dụ: "Sauce Labs Backpack") */
    public void themSanPhamTheoTen(String tenSanPham) {
        for (WebElement item : inventoryItems) {
            String name = item.findElement(By.cssSelector(".inventory_item_name")).getText().trim();
            if (name.equalsIgnoreCase(tenSanPham.trim())) {
                item.findElement(By.cssSelector("button.btn_inventory")).click();
                return;
            }
        }
        throw new NoSuchElementException("Không tìm thấy sản phẩm: " + tenSanPham);
    }

    /** Thêm N sản phẩm đầu tiên trong danh sách */
    public void themNSanPhamDauTien(int n) {
        int count = Math.min(n, inventoryItems.size());
        for (int i = 0; i < count; i++) {
            inventoryItems.get(i).findElement(By.cssSelector("button.btn_inventory")).click();
        }
    }

    /** Xóa hết sản phẩm đã add (các button đang là Remove) */
    public void xoaHetSanPhamDaThem() {
        for (WebElement item : inventoryItems) {
            WebElement btn = item.findElement(By.cssSelector("button.btn_inventory"));
            if (btn.getText().trim().equalsIgnoreCase("Remove")) {
                btn.click();
            }
        }
    }

    /** Badge giỏ hàng: nếu không có badge -> 0 */
    public int laySoLuongBadge() {
        if (cartBadges == null || cartBadges.isEmpty()) return 0;
        return Integer.parseInt(cartBadges.get(0).getText().trim());
    }

    public void moGioHang() {
        cartLink.click();
    }

    /** Sort theo value của saucedemo: az, za, lohi, hilo */
    public void sortSanPham(String optionValue) {
        new Select(sortDropdown).selectByValue(optionValue);
    }

    public List<String> layDanhSachTenSanPham() {
        List<String> names = new ArrayList<>();
        for (WebElement item : inventoryItems) {
            names.add(item.findElement(By.cssSelector(".inventory_item_name")).getText().trim());
        }
        return names;
    }

    public List<Double> layDanhSachGiaSanPham() {
        List<Double> prices = new ArrayList<>();
        for (WebElement item : inventoryItems) {
            String p = item.findElement(By.cssSelector(".inventory_item_price")).getText().trim(); // "$29.99"
            prices.add(parsePrice(p));
        }
        return prices;
    }

    private double parsePrice(String priceText) {
        return Double.parseDouble(priceText.replace("$", "").trim());
    }
}