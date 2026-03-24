package tests.bai1;

import base.ApiBaseTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public
class ReqresGetApiTest extends ApiBaseTest {

    @Test(description = "GET /api/users?page=1 should return page 1 with at least one user") public void getUsersPage1_shouldReturnPage1AndNonEmptyData() {
        given()
            .spec(requestSpec)
            .queryParam("page", 1)
            .when()
            .get("/users")
            .then()
            .spec(jsonResponseSpec)
            .statusCode(200)
            .body("page", equalTo(1))
            .body("total_pages", greaterThan(0))
            .body("data.size()", greaterThanOrEqualTo(1));
    }

    @Test(description = "GET /api/users?page=2 should return users with required fields") public void getUsersPage2_shouldContainRequiredFieldsForEachUser() {
        given()
            .spec(requestSpec)
            .queryParam("page", 2)
            .when()
            .get("/users")
            .then()
            .spec(jsonResponseSpec)
            .statusCode(200)
            .body("page", equalTo(2))
            .body("data.id", everyItem(notNullValue()))
            .body("data.email", everyItem(not(emptyOrNullString())))
            .body("data.first_name", everyItem(not(emptyOrNullString())))
            .body("data.last_name", everyItem(not(emptyOrNullString())))
            .body("data.avatar", everyItem(not(emptyOrNullString())));
    }

    @Test(description = "GET /api/users/3 should return valid reqres email and non-empty first name") public void getUser3_shouldReturnValidReqresEmailAndFirstName() {
        given()
            .spec(requestSpec)
            .when()
            .get("/users/3")
            .then()
            .spec(jsonResponseSpec)
            .statusCode(200)
            .body("data.id", equalTo(3))
            .body("data.email", endsWith("@reqres.in"))
            .body("data.first_name", not(emptyOrNullString()));
    }

    @Test(description = "GET /api/users/9999 should return 404 and empty object") public void getNonExistingUser_shouldReturn404AndEmptyBody() {
        given()
            .spec(requestSpec)
            .when()
            .get("/users/9999")
            .then()
            .spec(jsonResponseSpec)
            .statusCode(404)
            .body("$", anEmptyMap());
    }
}
