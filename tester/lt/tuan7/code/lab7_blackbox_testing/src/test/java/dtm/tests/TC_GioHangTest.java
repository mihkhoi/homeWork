package dtm.tests;

import dtm.base.BaseTest;
import dtm.pages.CartPage;
import dtm.pages.InventoryPage;
import dtm.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

public class TC_GioHangTest extends BaseTest {

    @BeforeMethod
    public void dangNhap() {
        getDriver().get("https://www.saucedemo.com/");
        new LoginPage(getDriver()).dangNhap("standard_user", "secret_sauce");
        Assert.assertTrue(getDriver().getCurrentUrl().contains("/inventory.html"), "Login không vào inventory");
    }

    @Test(groups = {"smoke"}, description = "TC_CART_001: Thêm 1 sản phẩm -> badge = 1")
    public void them1SanPham() {
        InventoryPage inv = new InventoryPage(getDriver());
        inv.themNSanPhamDauTien(1);
        Assert.assertEquals(inv.laySoLuongBadge(), 1);
    }

    @Test(groups = {"smoke"}, description = "TC_CART_002: Thêm 3 sản phẩm -> badge = 3")
    public void them3SanPham() {
        InventoryPage inv = new InventoryPage(getDriver());
        inv.themNSanPhamDauTien(3);
        Assert.assertEquals(inv.laySoLuongBadge(), 3);
    }

    @Test(groups = {"regression"}, description = "TC_CART_003: Xóa hết -> giỏ trống, badge = 0")
    public void xoaHetSanPham() {
        InventoryPage inv = new InventoryPage(getDriver());
        inv.themNSanPhamDauTien(2);
        Assert.assertEquals(inv.laySoLuongBadge(), 2);

        inv.moGioHang();
        CartPage cart = new CartPage(getDriver());
        cart.removeHet();
        Assert.assertEquals(cart.demSoItemTrongGio(), 0);

        // quay lại inventory để check badge (continue shopping)
        cart.continueShopping();
        InventoryPage inv2 = new InventoryPage(getDriver());
        Assert.assertEquals(inv2.laySoLuongBadge(), 0);
    }

    @Test(groups = {"regression"}, description = "TC_CART_004: Sort giá tăng dần (lohi) đúng thứ tự")
    public void sortGiaTangDan() {
        InventoryPage inv = new InventoryPage(getDriver());
        inv.sortSanPham("lohi");
        List<Double> prices = inv.layDanhSachGiaSanPham();

        for (int i = 1; i < prices.size(); i++) {
            Assert.assertTrue(prices.get(i) >= prices.get(i - 1), "Giá không tăng dần: " + prices);
        }
    }
}