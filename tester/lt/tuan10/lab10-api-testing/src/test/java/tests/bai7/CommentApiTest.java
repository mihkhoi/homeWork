package tests.bai7;

import base.JsonPlaceholderBaseTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public
class CommentApiTest extends JsonPlaceholderBaseTest {

    @Test(description = "GET /posts/1/comments should return 5 comments for postId 1") public void getCommentsByPost_shouldReturn5Comments() {
        given()
            .spec(requestSpec)
            .when()
            .get("/posts/1/comments")
            .then()
            .spec(jsonResponseSpec)
            .statusCode(200)
            .time(lessThan(3000L))
            .body("size()", equalTo(5))
            .body("postId", everyItem(equalTo(1)))
            .body("id", everyItem(notNullValue()))
            .body("name", everyItem(not(emptyOrNullString())))
            .body("email", everyItem(not(emptyOrNullString())))
            .body("body", everyItem(not(emptyOrNullString())));
    }
}
