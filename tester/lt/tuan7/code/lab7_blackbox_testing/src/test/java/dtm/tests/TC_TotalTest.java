package dtm.tests;

import dtm.base.BaseTest;
import dtm.pages.*;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TC_TotalTest extends BaseTest {

    @Test(groups = {"regression"}, description = "TC_CART_010: Kiểm tra Item total, Tax 8%, Total (delta 0.01)")
    public void kiemTraTinhToanTongTien() {
        getDriver().get("https://www.saucedemo.com/");
        new LoginPage(getDriver()).dangNhap("standard_user", "secret_sauce");
        Assert.assertTrue(getDriver().getCurrentUrl().contains("/inventory.html"));

        InventoryPage inv = new InventoryPage(getDriver());
        inv.themNSanPhamDauTien(3); // thêm 3 sản phẩm bất kỳ
        inv.moGioHang();

        CartPage cart = new CartPage(getDriver());
        cart.checkout();

        CheckoutStep1Page s1 = new CheckoutStep1Page(getDriver());
        s1.nhapFirstName("A");
        s1.nhapLastName("B");
        s1.nhapZip("700000");
        s1.continueCheckout();

        CheckoutStep2Page s2 = new CheckoutStep2Page(getDriver());

        double itemTotal = s2.getItemTotal();
        double tax = s2.getTax();
        double total = s2.getTotal();

        double expectedTax = itemTotal * 0.08;
        double expectedTotal = itemTotal + expectedTax;

        // delta 0.01 theo yêu cầu đề
        Assert.assertTrue(Math.abs(tax - expectedTax) < 0.01,
                "Tax sai. expected=" + expectedTax + " actual=" + tax);

        Assert.assertTrue(Math.abs(total - expectedTotal) < 0.01,
                "Total sai. expected=" + expectedTotal + " actual=" + total);
    }
}