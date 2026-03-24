package tests.bai3;

import base.ApiBaseTest;
import models.reqres.CreateUserRequest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public
class ReqresSchemaApiTest extends ApiBaseTest {

    @Test(description = "GET /api/users should match user-list schema") public void listUsers_shouldMatchUserListSchema() {
        given()
            .spec(requestSpec)
            .queryParam("page", 1)
            .when()
            .get("/users")
            .then()
            .spec(jsonResponseSpec)
            .statusCode(200)
            .body(matchesJsonSchemaInClasspath("schemas/user-list-schema.json"));
    }

    @Test(description = "GET /api/users/2 should match single user schema") public void singleUser_shouldMatchUserSchema() {
        given()
            .spec(requestSpec)
            .when()
            .get("/users/2")
            .then()
            .spec(jsonResponseSpec)
            .statusCode(200)
            .body(matchesJsonSchemaInClasspath("schemas/user-schema.json"));
    }

    @Test(description = "POST /api/users should match create-user schema") public void createUser_shouldMatchCreateUserSchema() {
        CreateUserRequest request = new CreateUserRequest("Nemlui", "Tester");

        given()
            .spec(requestSpec)
            .body(request)
            .when()
            .post("/users")
            .then()
            .spec(jsonResponseSpec)
            .statusCode(201)
            .body(matchesJsonSchemaInClasspath("schemas/create-user-schema.json"));
    }
}
