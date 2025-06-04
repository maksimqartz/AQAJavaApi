package tests;

import io.qameta.allure.*;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Epic("Users Data Cases")
@Feature("Users")
@Owner("qa-user")
@Severity(SeverityLevel.BLOCKER)
@Link(name = "API Docs", url = "https://playground.learnqa.ru/api/map")
public class UserGetTest extends BaseTestCase {

    @Test
    @Description("This test checks getting only username by ID without authorization")
    @DisplayName("Test Get User Data w/o Auth")
    @Story("GET-1")
    @Tags({@Tag("positive"), @Tag("user"), @Tag("get")})
    public void testGetUserDataNotAuth() {
        int userId = 2;

        Response responseUserData = apiCoreRequest
                .makeGetRequest(BASE_URL + "user/" + userId);

        Assertions.assertJsonHasField(responseUserData, "username");
        Assertions.assertJsonHasNoField(responseUserData, "email");
        Assertions.assertJsonHasNoField(responseUserData, "firstName");
        Assertions.assertJsonHasNoField(responseUserData, "lastName");
    }

    @Test
    @Description("This test checks getting user full data by ID with authorization as the same user")
    @DisplayName("Test Get User Data with Auth as Same User")
    @Story("GET-1")
    @Tags({@Tag("positive"), @Tag("user"), @Tag("get")})
    public void testGetUserDetailsAuthAsSameUser() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequest.makePostRequest(
                BASE_URL + "user/login",
                authData);

        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");

        Response responseUserData = apiCoreRequest.makeGetRequestWithCookieAndHeader(
                BASE_URL + "user/2",
                header,
                cookie);

        String[] expectedFields = {"username", "email", "firstName", "lastName"};
        Assertions.assertJsonHasFields(responseUserData, expectedFields);
    }

    @Test
    @Description("This test checks getting only username by ID with authorization as another user")
    @DisplayName("Test Get User Data with Auth as Another User")
    @Story("GET-2")
    @Tags({@Tag("negative"), @Tag("user"), @Tag("get")})
    public void testGetAnotherUserDetails() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequest.makePostRequest(
                BASE_URL + "user/login",
                authData);

        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");

        Response responseUserData = apiCoreRequest.makeGetRequestWithCookieAndHeader(
                BASE_URL + "user/1",
                header,
                cookie);

        Assertions.assertJsonHasField(responseUserData, "username");
        Assertions.assertJsonHasNoField(responseUserData, "email");
        Assertions.assertJsonHasNoField(responseUserData, "firstName");
        Assertions.assertJsonHasNoField(responseUserData, "lastName");
    }
}