package tests.bai4;

import base.ApiBaseTest;
import io.restassured.response.ValidatableResponse;
import models.reqres.LoginRequest;
import models.reqres.LoginResponse;
import models.reqres.RegisterResponse;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.emptyOrNullString;

public
class ReqresAuthorizationApiTest extends ApiBaseTest {

    @Test(description = "POST /api/login should return token for valid credential") public void login_shouldReturnTokenForValidCredential() {
        LoginRequest request = new LoginRequest(getDemoEmail(), getDemoPassword());

        LoginResponse response =
            given()
                .spec(requestSpec)
                .body(request)
                .when()
                .post("/login")
                .then()
                .spec(jsonResponseSpec)
                .statusCode(200)
                .extract()
                .as(LoginResponse.class);

        Assert.assertNotNull(response.token);
        Assert.assertFalse(response.token.isBlank());
    }

    @Test(description = "POST /api/login without password should return Missing password") public void loginWithoutPassword_shouldReturn400() {
        given()
            .spec(requestSpec)
            .body(new LoginRequest(getDemoEmail(), null))
            .when()
            .post("/login")
            .then()
            .spec(jsonResponseSpec)
            .statusCode(400)
            .body("error", containsString("Missing password"));
    }

    @Test(description = "POST /api/login without email should return Missing email or username") public void loginWithoutEmail_shouldReturn400() {
        given()
            .spec(requestSpec)
            .body(new LoginRequest(null, getDemoPassword()))
            .when()
            .post("/login")
            .then()
            .spec(jsonResponseSpec)
            .statusCode(400)
            .body("error", containsString("Missing email or username"));
    }

    @Test(description = "POST /api/register should return id and token") public void register_shouldReturnIdAndToken() {
        LoginRequest request = new LoginRequest(getDemoEmail(), getDemoPassword());

        RegisterResponse response =
            given()
                .spec(requestSpec)
                .body(request)
                .when()
                .post("/register")
                .then()
                .spec(jsonResponseSpec)
                .statusCode(200)
                .extract()
                .as(RegisterResponse.class);

        Assert.assertNotNull(response.id);
        Assert.assertNotNull(response.token);
        Assert.assertFalse(response.token.isBlank());
    }

    @Test(description = "POST /api/register without password should return Missing password") public void registerWithoutPassword_shouldReturn400() {
        given()
            .spec(requestSpec)
            .body(new LoginRequest(getDemoEmail(), null))
            .when()
            .post("/register")
            .then()
            .spec(jsonResponseSpec)
            .statusCode(400)
            .body("error", containsString("Missing password"));
    }

    @DataProvider(name = "loginScenarios") public Object[][] loginScenarios() {
        return new Object[][]{
            {getDemoEmail(), getDemoPassword(), 200, null},
            {getDemoEmail(), null, 400, "Missing password"},
            {null, getDemoPassword(), 400, "Missing email or username"},
            {"notexist@reqres.in", "wrongpass", 400, "user not found"},
            {"invalid-email", "pass123", 400, "user not found"}};
    }

    @Test(
        dataProvider = "loginScenarios",
        description = "Data-driven login scenarios should return expected status and error") public void loginScenarios_shouldBehaveAsExpected(String email, String password, int expectedStatus, String expectedError) {
        LoginRequest request = new LoginRequest(email, password);

        ValidatableResponse response =
            given()
                .spec(requestSpec)
                .body(request)
                .when()
                .post("/login")
                .then()
                .spec(jsonResponseSpec)
                .statusCode(expectedStatus);

        if (expectedError != null) {
            response.body("error", containsString(expectedError));
        } else {
            response.body("token", not(emptyOrNullString()));
        }
    }
}
