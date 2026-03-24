package tests.bai6;

import base.ApiBaseTest;
import base.UiBaseTest;
import models.reqres.LoginRequest;
import pages.InventoryPage;
import pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public
class ApiUiPartATest extends UiBaseTest {
  private
    String token;
  private
    boolean apiLoginPassed;
  private
    ApiBaseTest api;

    @BeforeMethod(alwaysRun = true) public void setupApiPrecondition() {
        api = new ApiBaseTest();
        api.setupApiSpec();

        try {
            token = given()
                        .spec(api.getRequestSpec())
                        .body(new LoginRequest(api.getDemoEmail(), api.getDemoPassword()))
                        .when()
                        .post("/login")
                        .then()
                        .spec(api.getJsonResponseSpec())
                        .statusCode(200)
                        .extract()
                        .jsonPath()
                        .getString("token");

            apiLoginPassed = token != null && !token.isBlank();
            System.out.println("[API PRECONDITION] token = " + token);
        } catch (Exception e) {
            apiLoginPassed = false;
            token = null;
            e.printStackTrace();
        }
    }

    @Test(description = "API login precondition should return token") public void apiLoginPrecondition_shouldReturnToken() {
        Assert.assertTrue(apiLoginPassed, "API login phải pass trước khi chạy UI");
        Assert.assertNotNull(token);
        Assert.assertFalse(token.isBlank());
    }

    @Test(
        description = "UI login should navigate to inventory only when API precondition passed",
        dependsOnMethods = "apiLoginPrecondition_shouldReturnToken") public void uiLogin_shouldNavigateToInventory() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();
        loginPage.login("standard_user", "secret_sauce");

        Assert.assertTrue(driver.getCurrentUrl().contains("inventory"));
        Assert.assertEquals(driver.getTitle(), "Swag Labs");

        InventoryPage inventoryPage = new InventoryPage(driver);
        Assert.assertTrue(inventoryPage.isLoaded());
    }
}
