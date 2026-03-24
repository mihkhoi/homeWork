package tests.bai6;

import base.ApiBaseTest;
import base.UiBaseTest;
import pages.CartPage;
import pages.InventoryPage;
import pages.LoginPage;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public
class ApiUiPartBTest extends UiBaseTest {

  private
    boolean isApiAlive;
  private
    ApiBaseTest api;

    @BeforeMethod(alwaysRun = true) public void checkApiHealth() {
        api = new ApiBaseTest();
        api.setupApiSpec();

        try {
            given()
                .spec(api.getRequestSpec())
                .when()
                .get("/users")
                .then()
                .spec(api.getJsonResponseSpec())
                .statusCode(200);

            isApiAlive = true;
        } catch (Exception e) {
            isApiAlive = false;
            e.printStackTrace();
        }
    }

    @Test(description = "UI cart flow should be skipped when ReqRes API is not alive") public void uiCartFlow_shouldDependOnApiHealth() {
        if (!isApiAlive) {
            throw new SkipException("ReqRes API down -> skip UI test");
        }

        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();
        loginPage.login("standard_user", "secret_sauce");

        InventoryPage inventoryPage = new InventoryPage(driver);
        Assert.assertTrue(inventoryPage.isLoaded(), "Trang inventory phải load được");

        inventoryPage.addFirstNItems(2);
        Assert.assertEquals(inventoryPage.getCartItemCount(), 2);

        CartPage cartPage = inventoryPage.goToCart();
        Assert.assertEquals(cartPage.getItemCount(), 2, "Giỏ hàng phải có đúng 2 sản phẩm");
    }
}
