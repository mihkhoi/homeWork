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

        String apiKey = System.getenv("REQRES_API_KEY");
        if (shouldSendReqresApiKey() && apiKey != null && !apiKey.isBlank()) {
            builder.addHeader("x-api-key", apiKey);
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
        return getBaseUri().contains("reqres.in");
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
