package tests.bai2;

import base.ApiBaseTest;
import models.reqres.CollectionRecordRequest;
import models.reqres.CollectionRecordResponse;
import models.reqres.CreateUserRequest;
import models.reqres.CreateUserResponse;
import models.reqres.UpdateUserRequest;
import models.reqres.UpdateUserResponse;
import org.testng.Assert;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public
class ReqresCrudApiTest extends ApiBaseTest {

    @Test(description = "POST /api/users should create a user and return id with createdAt") public void createUser_shouldReturn201AndCreatedFields() {
        CreateUserRequest request = new CreateUserRequest("Nemlui", "QA Intern");

        CreateUserResponse response =
            given()
                .spec(requestSpec)
                .body(request)
                .when()
                .post("/users")
                .then()
                .spec(jsonResponseSpec)
                .statusCode(201)
                .extract()
                .as(CreateUserResponse.class);

        Assert.assertEquals(response.name, "Nemlui");
        Assert.assertEquals(response.job, "QA Intern");
        Assert.assertNotNull(response.id);
        Assert.assertFalse(response.id.isBlank());
        Assert.assertNotNull(response.createdAt);
        Assert.assertFalse(response.createdAt.isBlank());
    }

    @Test(description = "PUT /api/users/2 should update full user payload and return updatedAt") public void updateUserWithPut_shouldReturnUpdatedFields() {
        UpdateUserRequest request = new UpdateUserRequest("Nemlui Updated", "QA Engineer");

        UpdateUserResponse response =
            given()
                .spec(requestSpec)
                .body(request)
                .when()
                .put("/users/2")
                .then()
                .spec(jsonResponseSpec)
                .statusCode(200)
                .extract()
                .as(UpdateUserResponse.class);

        Assert.assertEquals(response.name, "Nemlui Updated");
        Assert.assertEquals(response.job, "QA Engineer");
        Assert.assertNotNull(response.updatedAt);
        Assert.assertFalse(response.updatedAt.isBlank());
    }

    @Test(description = "PATCH /api/users/2 should update only provided field and return updatedAt") public void patchUser_shouldUpdateOnlySentField() {
        UpdateUserRequest request = new UpdateUserRequest(null, "Senior QA Engineer");

        UpdateUserResponse response =
            given()
                .spec(requestSpec)
                .body(request)
                .when()
                .patch("/users/2")
                .then()
                .spec(jsonResponseSpec)
                .statusCode(200)
                .extract()
                .as(UpdateUserResponse.class);

        Assert.assertEquals(response.job, "Senior QA Engineer");
        Assert.assertNotNull(response.updatedAt);
        Assert.assertFalse(response.updatedAt.isBlank());
    }

    @Test(description = "DELETE /api/users/2 should return 204 and empty body") public void deleteUser_shouldReturn204AndEmptyBody() {
        String body =
            given()
                .spec(requestSpec)
                .when()
                .delete("/users/2")
                .then()
                .spec(noContentResponseSpec)
                .statusCode(204)
                .extract()
                .asString();

        Assert.assertTrue(body == null || body.isBlank(), "DELETE response body phải rỗng");
    }

    @Test(description = "POST then GET should confirm created data using persistent ReqRes collection") public void createThenGet_shouldConfirmPersistedData() {
        String apiKey = System.getenv("REQRES_API_KEY");
        Assert.assertTrue(apiKey != null && !apiKey.isBlank(),
                          "Bài này cần REQRES_API_KEY vì demo /api/users không persist thật");

        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "Nemlui Persist");
        payload.put("job", "QA Student");

        CollectionRecordRequest request = new CollectionRecordRequest(payload);

        CollectionRecordResponse created =
            given()
                .spec(requestSpec)
                .body(request)
                .when()
                .post("/collections/lab10-users/records")
                .then()
                .spec(jsonResponseSpec)
                .statusCode(anyOf(is(200), is(201)))
                .extract()
                .as(CollectionRecordResponse.class);

        Assert.assertNotNull(created.id);
        Assert.assertNotNull(created.data);
        Assert.assertEquals(created.data.get("name"), "Nemlui Persist");
        Assert.assertEquals(created.data.get("job"), "QA Student");

        CollectionRecordResponse fetched =
            given()
                .spec(requestSpec)
                .when()
                .get("/collections/lab10-users/records/" + created.id)
                .then()
                .spec(jsonResponseSpec)
                .statusCode(200)
                .extract()
                .as(CollectionRecordResponse.class);

        Assert.assertEquals(fetched.id, created.id);
        Assert.assertEquals(fetched.data.get("name"), "Nemlui Persist");
        Assert.assertEquals(fetched.data.get("job"), "QA Student");
    }
}
