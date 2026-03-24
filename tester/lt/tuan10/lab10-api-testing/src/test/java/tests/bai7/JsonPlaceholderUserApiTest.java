package tests.bai7;

import base.JsonPlaceholderBaseTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

public
class JsonPlaceholderUserApiTest extends JsonPlaceholderBaseTest {

    @Test(description = "GET /users should return 10 users") public void getUsers_shouldReturn10Users() {
        given()
            .spec(requestSpec)
            .when()
            .get("/users")
            .then()
            .spec(jsonResponseSpec)
            .statusCode(200)
            .time(lessThan(3000L))
            .body("size()", equalTo(10));
    }

    @Test(description = "GET /users/1 should match user schema with nested address") public void getUser1_shouldMatchUserSchema() {
        given()
            .spec(requestSpec)
            .when()
            .get("/users/1")
            .then()
            .spec(jsonResponseSpec)
            .statusCode(200)
            .time(lessThan(3000L))
            .body("id", equalTo(1))
            .body(matchesJsonSchemaInClasspath("schemas/jsonplaceholder-user-schema.json"));
    }
}
