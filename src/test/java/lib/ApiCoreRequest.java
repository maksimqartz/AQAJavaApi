package lib;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiCoreRequest {

    @Step("Make a GET-request to URL: {url}")
    public Response makeGetRequest(String url) {
        return given()
                .filter(new AllureRestAssured())
                .get(url)
                .andReturn();
    }

    @Step("Make a GET-request with token and auth cookie to URL: {url}")
    public Response makeGetRequestWithCookieAndHeader(String url, String token, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .get(url)
                .andReturn();
    }

    @Step("Make a GET-request only with auth cookie to URL: {url}")
    public Response makeGetRequestOnlyWithCookie(String url, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .cookie("auth_sid", cookie)
                .get(url)
                .andReturn();
    }

    @Step("Make a GET-request only with token to URL: {url}")
    public Response makeGetRequestOnlyWithToken(String url, String token) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .get(url)
                .andReturn();
    }

    @Step("Make a POST-request to URL: {url}")
    public Response makePostRequest(String url, Map<String, String> authData) {
        return given()
                .filter(new AllureRestAssured())
                .body(authData)
                .post(url)
                .andReturn();
    }

    @Step("DELETE request to URL: {url}")
    public Response makeDeleteRequest(String url, String token, String cookie) {
        return given()
                .header("x-csrf-token", token)
                .cookie("auth_sid", cookie)
                .delete(url)
                .andReturn();
    }

    @Step("Make a PUT-request to URL: {url}")
    public Response makePutRequest(String url, Map<String, String> editData, String token, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .header("x-csrf-token", token)
                .cookie("auth_sid", cookie)
                .body(editData)
                .put(url)
                .andReturn();
    }

    @Step("Make a PUT-request to URL: {url} without auth")
    public Response makePutRequestWithoutAuth(String url, Map<String, String> editData) {
        return given()
                .filter(new AllureRestAssured())
                .body(editData)
                .put(url)
                .andReturn();
    }
}
