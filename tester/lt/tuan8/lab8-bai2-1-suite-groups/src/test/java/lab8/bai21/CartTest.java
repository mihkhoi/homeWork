package lab8.bai21;

import org.testng.Assert;
import org.testng.annotations.Test;

public
class CartTest {

    @Test(groups = {"smoke", "regression"}, description = "Smoke - them san pham vao gio") public void testAddToCart() {
        System.out.println("[" + Thread.currentThread().getId() + "] CartTest - testAddToCart");
        int cartCount = 1;
        Assert.assertEquals(cartCount, 1, "So luong san pham trong gio khong dung");
    }

    @Test(groups = {"regression"}, description = "Regression - xoa san pham khoi gio") public void testRemoveFromCart() {
        System.out.println("[" + Thread.currentThread().getId() + "] CartTest - testRemoveFromCart");
        boolean removed = true;
        Assert.assertTrue(removed, "Xoa san pham khoi gio that bai");
    }
}
