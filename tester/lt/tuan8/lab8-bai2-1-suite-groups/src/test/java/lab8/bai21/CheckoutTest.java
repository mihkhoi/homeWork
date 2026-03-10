package lab8.bai21;

import org.testng.Assert;
import org.testng.annotations.Test;

public
class CheckoutTest {

    @Test(groups = {"smoke", "regression"}, description = "Smoke - mo trang checkout") public void testOpenCheckoutPage() {
        System.out.println("[" + Thread.currentThread().getId() + "] CheckoutTest - testOpenCheckoutPage");
        String pageTitle = "Checkout";
        Assert.assertEquals(pageTitle, "Checkout", "Khong mo duoc trang checkout");
    }

    @Test(groups = {"regression"}, description = "Regression - thanh toan thanh cong") public void testCheckoutSuccess() {
        System.out.println("[" + Thread.currentThread().getId() + "] CheckoutTest - testCheckoutSuccess");
        boolean paymentStatus = true;
        Assert.assertTrue(paymentStatus, "Thanh toan khong thanh cong");
    }
}
