package tests.bai7;

import base.JsonPlaceholderBaseTest;
import models.jsonplaceholder.PostRequest;
import models.jsonplaceholder.PostResponse;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public
class PostApiTest extends JsonPlaceholderBaseTest {

    @Test(description = "GET /posts should return 100 posts") public void getAllPosts_shouldReturn100Records() {
        given()
            .spec(requestSpec)
            .when()
            .get("/posts")
            .then()
            .spec(jsonResponseSpec)
            .statusCode(200)
            .time(org.hamcrest.Matchers.lessThan(3000L))
            .body("size()", org.hamcrest.Matchers.equalTo(100));
    }

    @Test(description = "GET /posts/1 should return post id 1 and match schema") public void getSinglePost_shouldReturnPost1() {
        given()
            .spec(requestSpec)
            .when()
            .get("/posts/1")
            .then()
            .spec(jsonResponseSpec)
            .statusCode(200)
            .time(org.hamcrest.Matchers.lessThan(3000L))
            .body("id", org.hamcrest.Matchers.equalTo(1))
            .body(matchesJsonSchemaInClasspath("schemas/post-schema.json"));
    }

    @Test(description = "POST /posts should create a new post contract") public void createPost_shouldReturnCreatedPost() {
        PostRequest request = new PostRequest("Lab 10", "JSONPlaceholder create post", 1);

        PostResponse response =
            given()
                .spec(requestSpec)
                .body(request)
                .when()
                .post("/posts")
                .then()
                .spec(jsonResponseSpec)
                .statusCode(201)
                .time(org.hamcrest.Matchers.lessThan(3000L))
                .extract()
                .as(PostResponse.class);

        Assert.assertEquals(response.title, "Lab 10");
        Assert.assertEquals(response.body, "JSONPlaceholder create post");
        Assert.assertEquals(response.userId, Integer.valueOf(1));
        Assert.assertNotNull(response.id);
    }

    @Test(description = "PUT /posts/1 should update full post payload") public void updatePost_shouldReturnUpdatedPost() {
        PostRequest request = new PostRequest("Updated Title", "Updated Body", 1);

        PostResponse response =
            given()
                .spec(requestSpec)
                .body(request)
                .when()
                .put("/posts/1")
                .then()
                .spec(jsonResponseSpec)
                .statusCode(200)
                .time(org.hamcrest.Matchers.lessThan(3000L))
                .extract()
                .as(PostResponse.class);

        Assert.assertEquals(response.id, Integer.valueOf(1));
        Assert.assertEquals(response.title, "Updated Title");
        Assert.assertEquals(response.body, "Updated Body");
    }

    @Test(description = "DELETE /posts/1 should return success status") public void deletePost_shouldReturn200() {
        given()
            .spec(requestSpec)
            .when()
            .delete("/posts/1")
            .then()
            .spec(noContentResponseSpec)
            .statusCode(200)
            .time(org.hamcrest.Matchers.lessThan(3000L));
    }
}
