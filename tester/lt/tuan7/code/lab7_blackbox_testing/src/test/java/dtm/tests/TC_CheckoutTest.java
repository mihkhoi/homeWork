package dtm.tests;

import dtm.base.BaseTest;
import dtm.pages.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TC_CheckoutTest extends BaseTest {

    @BeforeMethod
    public void loginVaCo1ItemTrongGio() {
        getDriver().get("https://www.saucedemo.com/");
        new LoginPage(getDriver()).dangNhap("standard_user", "secret_sauce");
        Assert.assertTrue(getDriver().getCurrentUrl().contains("/inventory.html"), "Login không vào inventory");

        InventoryPage inv = new InventoryPage(getDriver());
        inv.themNSanPhamDauTien(1);
        inv.moGioHang();
        CartPage cart = new CartPage(getDriver());
        Assert.assertTrue(cart.demSoItemTrongGio() >= 1, "Giỏ hàng chưa có item");
        cart.checkout();
        Assert.assertTrue(getDriver().getCurrentUrl().contains("checkout-step-one"), "Không vào checkout step one");
    }

    @Test(groups = {"regression"}, description = "TC_CO_001: First Name trống -> báo lỗi")
    public void checkoutFirstNameTrong() {
        CheckoutStep1Page s1 = new CheckoutStep1Page(getDriver());
        s1.nhapFirstName("");
        s1.nhapLastName("Nguyen");
        s1.nhapZip("700000");
        s1.continueCheckout();

        String msg = s1.layThongBaoLoi();
        Assert.assertNotNull(msg);
        Assert.assertTrue(msg.toLowerCase().contains("first name"), "Sai lỗi: " + msg);
    }

    @Test(groups = {"regression"}, description = "TC_CO_002: Điền đủ -> sang Step2")
    public void checkoutThanhCongSangStep2() {
        CheckoutStep1Page s1 = new CheckoutStep1Page(getDriver());
        s1.nhapFirstName("A");
        s1.nhapLastName("B");
        s1.nhapZip("700000");
        s1.continueCheckout();

        Assert.assertTrue(getDriver().getCurrentUrl().contains("checkout-step-two"), "Không vào step two");
    }

    @Test(groups = {"smoke"}, description = "TC_CO_003: Finish -> checkout complete")
    public void checkoutFinish() {
        CheckoutStep1Page s1 = new CheckoutStep1Page(getDriver());
        s1.nhapFirstName("A");
        s1.nhapLastName("B");
        s1.nhapZip("700000");
        s1.continueCheckout();

        CheckoutStep2Page s2 = new CheckoutStep2Page(getDriver());
        s2.finish();

        Assert.assertTrue(getDriver().getCurrentUrl().contains("checkout-complete"), "Không vào complete");
        CheckoutCompletePage done = new CheckoutCompletePage(getDriver());
        Assert.assertTrue(done.getHeader().toLowerCase().contains("thank you"), "Sai message complete");
    }
}