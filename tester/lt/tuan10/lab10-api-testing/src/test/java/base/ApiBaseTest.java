package base;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;

import static org.hamcrest.Matchers.lessThan;

public
class ApiBaseTest {

    // DÁN KEY CỦA BẠN VÀO ĐÂY
  private
    static final String REQRES_API_KEY = "reqres_7e9abe50e2074205afe71a02e24762c3";

  protected
    RequestSpecification requestSpec;
  protected
    ResponseSpecification jsonResponseSpec;
  protected
    ResponseSpecification noContentResponseSpec;

    @BeforeClass(alwaysRun = true) public void setupApiSpec() {
        RequestSpecBuilder builder = new RequestSpecBuilder()
                                         .setBaseUri(getBaseUri())
                                         .setBasePath(getBasePath())
                                         .setContentType(ContentType.JSON)
                                         .addHeader("Accept", "application/json")
                                         .addHeader("User-Agent", "lab10-api-tests/1.0")
                                         .addFilter(new RequestLoggingFilter())
                                         .addFilter(new ResponseLoggingFilter());

        if (shouldSendReqresApiKey()) {
            builder.addHeader("x-api-key", REQRES_API_KEY);
        }

        requestSpec = builder.build();

        jsonResponseSpec = new ResponseSpecBuilder()
                               .expectResponseTime(lessThan(3000L))
                               .expectContentType(ContentType.JSON)
                               .build();

        noContentResponseSpec = new ResponseSpecBuilder()
                                    .expectResponseTime(lessThan(3000L))
                                    .build();
    }

  protected
    String getBaseUri() {
        return "https://reqres.in";
    }

  protected
    String getBasePath() {
        return "/api";
    }

  protected
    boolean shouldSendReqresApiKey() {
        return true;
    }

  public
    String getDemoEmail() {
        return "eve.holt@reqres.in";
    }

  public
    String getDemoPassword() {
        return "pistol";
    }

  public
    RequestSpecification getRequestSpec() {
        return requestSpec;
    }

  public
    ResponseSpecification getJsonResponseSpec() {
        return jsonResponseSpec;
    }

  public
    ResponseSpecification getNoContentResponseSpec() {
        return noContentResponseSpec;
    }
}
