package tests;

import io.qameta.allure.*;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

@Epic("Authorization Cases")
@Feature("Authorization")
@Owner("qa-user")
@Severity(SeverityLevel.CRITICAL)
@Link(name = "API Docs", url = "https://playground.learnqa.ru/api/map")
public class UserAuthTest extends BaseTestCase {

    String cookie;
    String header;
    int userIdOnAuth;

    @BeforeEach
    @Step("Login user before each test")
    public void loginUser() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequest
                .makePostRequest(BASE_URL + "user/login", authData);

        this.cookie = this.getCookie(responseGetAuth, "auth_sid");
        this.header = this.getHeader(responseGetAuth, "x-csrf-token");
        this.userIdOnAuth = this.getIntFromJson(responseGetAuth, "user_id");
    }

    @Test
    @Description("This test successfully checks user authorization by email and password.")
    @DisplayName("Test Positive User Authorization")
    @Story("AUTH-1")
    @Tags({@Tag("positive"), @Tag("user"), @Tag("auth")})
    public void testUserAuth() {

        Response responseCheckAuth = apiCoreRequest
                .makeGetRequestWithCookieAndHeader(
                        BASE_URL + "user/auth",
                        this.header,
                        this.cookie);

        Assertions.assertJsonByName(responseCheckAuth, "user_id", this.userIdOnAuth);
    }

    @ParameterizedTest
    @ValueSource(strings = {"cookie", "headers"})
    @Description("This test checks authorization status w/o sending auth cookie or token.")
    @DisplayName("Test Negative User Authorization")
    @Story("AUTH-2")
    @Tags({@Tag("negative"), @Tag("user"), @Tag("auth"), @Tag("parameterized")})
    public void testNegativeAuthUser(String condition) {
        if (condition.equals("cookie")) {
            Response responseForCheck = apiCoreRequest.makeGetRequestOnlyWithCookie(
                    BASE_URL + "user/auth",
                    this.cookie
            );
            Assertions.assertJsonByName(responseForCheck, "user_id", 0);
        } else if (condition.equals("headers")) {
            Response responseForCheck = apiCoreRequest.makeGetRequestOnlyWithToken(
                    BASE_URL + "user/auth",
                    this.header
            );
            Assertions.assertJsonByName(responseForCheck, "user_id", 0);
        } else {
            throw new IllegalArgumentException("Condition value is not known: " + condition);
        }
    }
}
