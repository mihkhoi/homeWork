package lab8.bai21;

import org.testng.Assert;
import org.testng.annotations.Test;

public
class LoginTest {

    @Test(groups = {"smoke", "regression"}, description = "Smoke - login thanh cong") public void testLoginSuccess() {
        System.out.println("[" + Thread.currentThread().getId() + "] LoginTest - testLoginSuccess");
        Assert.assertTrue(true, "Dang nhap hop le phai thanh cong");
    }

    @Test(groups = {"regression"}, description = "Regression - login sai mat khau") public void testLoginWrongPassword() {
        System.out.println("[" + Thread.currentThread().getId() + "] LoginTest - testLoginWrongPassword");
        String actualMessage = "Username and password do not match";
        String expectedMessage = "Username and password do not match";
        Assert.assertEquals(actualMessage, expectedMessage, "Thong bao loi dang nhap sai khong dung");
    }
}
