package tests.bai5;

import base.ApiBaseTest;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.reqres.CreateUserRequest;
import models.reqres.LoginRequest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;

public
class ReqresPerformanceApiTest extends ApiBaseTest {

    @DataProvider(name = "apiSlaScenarios") public Object[][] apiSlaScenarios() {
        return new Object[][]{
            {"GET", "/users", 200, 2000L},
            {"GET", "/users/2", 200, 1500L},
            {"POST", "/users", 201, 3000L},
            {"POST", "/login", 200, 2000L},
            {"DELETE", "/users/2", 204, 1000L}};
    }

    @Step("Gọi {method} {endpoint} - SLA: {maxMs}ms") private Response callApi(String method, String endpoint, long maxMs) {
        long start = System.currentTimeMillis();
        Response response;

        switch (method) {
        case "GET":
            response = given().spec(requestSpec).when().get(endpoint);
            break;
        case "POST":
            if ("/users".equals(endpoint)) {
                response = given().spec(requestSpec).body(new CreateUserRequest("Perf User", "Perf Tester")).when().post(endpoint);
            } else {
                response = given().spec(requestSpec).body(new LoginRequest(getDemoEmail(), getDemoPassword())).when().post(endpoint);
            }
            break;
        case "DELETE":
            response = given().spec(requestSpec).when().delete(endpoint);
            break;
        default:
            throw new IllegalArgumentException("Unsupported method: " + method);
        }

        long elapsed = System.currentTimeMillis() - start;
        System.out.printf("[SLA] %s %s -> %dms%n", method, endpoint, elapsed);
        Assert.assertTrue(elapsed < maxMs, "Response time " + elapsed + "ms vượt SLA " + maxMs + "ms");
        return response;
    }

    @Test(dataProvider = "apiSlaScenarios", description = "All main ReqRes endpoints should satisfy SLA") public void endpoints_shouldMeetSla(String method, String endpoint, int expectedStatus, long maxMs) {
        Response response = callApi(method, endpoint, maxMs);
        Assert.assertEquals(response.getStatusCode(), expectedStatus);

        if ("GET".equals(method) && "/users".equals(endpoint)) {
            Assert.assertTrue(response.jsonPath().getList("data").size() >= 1);
        }

        if ("GET".equals(method) && "/users/2".equals(endpoint)) {
            Assert.assertEquals((int)response.jsonPath().getInt("data.id"), 2);
        }

        if ("POST".equals(method) && "/users".equals(endpoint)) {
            Assert.assertNotNull(response.jsonPath().getString("id"));
        }

        if ("POST".equals(method) && "/login".equals(endpoint)) {
            Assert.assertNotNull(response.jsonPath().getString("token"));
        }
    }

    @Test(description = "GET /api/users/2 should be monitored across 10 runs") public void monitorSingleEndpointAcross10Runs_shouldPrintAverageMinMax() {
        List<Long> samples = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            long start = System.currentTimeMillis();
            given()
                .spec(requestSpec)
                .when()
                .get("/users/2")
                .then()
                .spec(jsonResponseSpec)
                .statusCode(200);

            long elapsed = System.currentTimeMillis() - start;
            samples.add(elapsed);
            System.out.printf("[MONITOR] Run %d -> %dms%n", i + 1, elapsed);
        }

        long min = Collections.min(samples);
        long max = Collections.max(samples);
        double avg = samples.stream().mapToLong(Long::longValue).average().orElse(0.0);

        System.out.printf("[MONITOR] average=%.2fms, min=%dms, max=%dms%n", avg, min, max);
        Assert.assertTrue(max < 3000, "Tất cả lần chạy phải < 3000ms");
    }
}
