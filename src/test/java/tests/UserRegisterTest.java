package tests;

import io.qameta.allure.*;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

@Epic("Registration Cases")
@Feature("Registration")
@Owner("qa-user")
@Severity(SeverityLevel.CRITICAL)
@Link(name = "API Docs", url = "https://playground.learnqa.ru/api/map")
public class UserRegisterTest extends BaseTestCase {

    @Test
    @Description("This test checks successful user registration with a unique email.")
    @DisplayName("Test User Registration Successfully")
    @Story("REG-1")
    @Tags({@Tag("positive"), @Tag("user"), @Tag("register")})
    public void testCreateUserSuccessfully() {
        Map<String, String> userData = DataGenerator.getRegistrationalData();

        Response responseCreateUser = apiCoreRequest.makePostRequest(BASE_URL + "user", userData);

        Assertions.assertResponseCodeEquals(responseCreateUser, 200);
        Assertions.assertJsonHasField(responseCreateUser, "id");
    }

    @Test
    @Description("This negative test checks user registration with an existing email.")
    @DisplayName("Test User Registration Not Successfully: Users already exists")
    @Story("REG-2")
    @Tags({@Tag("negative"), @Tag("user"), @Tag("register")})
    public void testCreateUserWithExistingEmail() {
        String email = "vinkotov@example.com";

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGenerator.getRegistrationalData(userData);

        Response responseCreateUser = apiCoreRequest.makePostRequest(BASE_URL + "user", userData);

        Assertions.assertResponseCodeEquals(responseCreateUser, 400);
        Assertions.assertResponseTextEquals(responseCreateUser, "Users with email '" + email + "' already exists");
    }

    @Test
    @Description("This negative test checks user registration w/o @ in email.")
    @DisplayName("Test User Registration Not Successfully: Invalid email format")
    @Story("REG-3")
    @Tags({@Tag("negative"), @Tag("user"), @Tag("register")})
    public void testCreateUserWithoutAtEmail() {
        String email = "vinkotov.at.example.com";

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGenerator.getRegistrationalData(userData);

        Response responseCreateUser = apiCoreRequest.makePostRequest(BASE_URL + "user", userData);

        Assertions.assertResponseCodeEquals(responseCreateUser, 400);
        Assertions.assertResponseTextEquals(responseCreateUser, "Invalid email format");
    }

    @ParameterizedTest
    @ValueSource(strings = {"username", "firstName", "lastName", "email", "password"})
    @Description("This negative test checks user registration w/o one of the params.")
    @DisplayName("Test User Registration Not Successfully: Param is missing")
    @Story("REG-4")
    @Tags({@Tag("negative"), @Tag("user"), @Tag("register"), @Tag("parameterized")})
    public void testCreateUserWithoutParam(String param) {
        Map<String, String> userData = DataGenerator.getRegistrationalData();
        userData.remove(param);

        Response responseCreateUser = apiCoreRequest.makePostRequest(BASE_URL + "user", userData);

        Assertions.assertResponseCodeEquals(responseCreateUser, 400);
        Assertions.assertResponseTextEquals(responseCreateUser, "The following required params are missed: " + param);
    }

    @ParameterizedTest
    @ValueSource(strings = {"short", "long"})
    @Description("This negative test checks user registration with too short & too long firstName.")
    @DisplayName("Test User Registration Not Successfully: Too short or too long firstName")
    @Story("REG-5")
    @Tags({@Tag("negative"), @Tag("user"), @Tag("register"), @Tag("parameterized")})
    public void testCreateUserWithBadName(String length) {
        String param = "firstName";
        int len = length.equals("short") ? 1 : 251; // 1 for short, 251 for long
        Map<String, String> userData = DataGenerator.getRegistrationalDataWithNeededLength(param, len);

        Response responseCreateUser = apiCoreRequest.makePostRequest(BASE_URL + "user", userData);

        Assertions.assertResponseCodeEquals(responseCreateUser, 400);
        Assertions.assertResponseTextEquals(responseCreateUser, "The value of '" + param + "' field is too " + length);
    }
}
