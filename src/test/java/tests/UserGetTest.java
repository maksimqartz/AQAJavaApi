package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Epic("Users Data Cases")
@Feature("Users")
public class UserGetTest extends BaseTestCase {

    @Description("This test checks getting only username by ID without authorization")
    @DisplayName("Test Get User Data w/o Auth")
    @Test
    public void testGetUserDataNotAuth() {
        int userId = 2;

        Response responseUserData = apiCoreRequest
                .makeGetRequest("https://playground.learnqa.ru/api/user/" + userId);

        Assertions.assertJsonHasField(responseUserData, "username");
        Assertions.assertJsonHasNoField(responseUserData, "email");
        Assertions.assertJsonHasNoField(responseUserData, "firstName");
        Assertions.assertJsonHasNoField(responseUserData, "lastName");
    }

    @Description("This test checks getting user full data by ID with authorization as the same user")
    @DisplayName("Test Get User Data with Auth as Same User")
    @Test
    public void testGetUserDetailsAuthAsSameUser() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequest.makePostRequest(
                "https://playground.learnqa.ru/api/user/login",
                authData);

        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");

        Response responseUserData = apiCoreRequest.makeGetRequestWithCookieAndHeader(
                "https://playground.learnqa.ru/api/user/2",
                header,
                cookie);

        String[] expectedFields = {"username", "email", "firstName", "lastName"};
        Assertions.assertJsonHasFields(responseUserData, expectedFields);
    }

    @Description("This test checks getting only username by ID with authorization as another user")
    @DisplayName("Test Get User Data with Auth as Another User")
    @Test
    public void testGetAnotherUserDetails() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequest.makePostRequest(
                "https://playground.learnqa.ru/api/user/login",
                authData);

        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");

        Response responseUserData = apiCoreRequest.makeGetRequestWithCookieAndHeader(
                "https://playground.learnqa.ru/api/user/1",
                header,
                cookie);

        Assertions.assertJsonHasField(responseUserData, "username");
        Assertions.assertJsonHasNoField(responseUserData, "email");
        Assertions.assertJsonHasNoField(responseUserData, "firstName");
        Assertions.assertJsonHasNoField(responseUserData, "lastName");
    }
}