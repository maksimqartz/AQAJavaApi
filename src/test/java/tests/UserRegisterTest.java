package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

@Epic("Registration Cases")
@Feature("Registration")
public class UserRegisterTest extends BaseTestCase {

    @Description("This test checks successful user registration with a unique email.")
    @DisplayName("Test User Registration Successfully")
    @Test
    public void testCreateUserSuccessfully() {
        Map<String, String> userData = DataGenerator.getRegistrationalData();

        Response responseCreateUser = apiCoreRequest.makePostRequest("https://playground.learnqa.ru/api/user", userData);

        Assertions.assertResponseCodeEquals(responseCreateUser, 200);
        Assertions.assertJsonHasField(responseCreateUser, "id");
    }

    @Description("This negative test checks user registration with an existing email.")
    @DisplayName("Test User Registration Not Successfully: Users already exists")
    @Test
    public void testCreateUserWithExistingEmail() {
        String email = "vinkotov@example.com";

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGenerator.getRegistrationalData(userData);

        Response responseCreateUser = apiCoreRequest.makePostRequest("https://playground.learnqa.ru/api/user", userData);

        Assertions.assertResponseCodeEquals(responseCreateUser, 400);
        Assertions.assertResponseTextEquals(responseCreateUser, "Users with email '" + email + "' already exists");
    }

    @Description("This negative test checks user registration w/o @ in email.")
    @DisplayName("Test User Registration Not Successfully: Invalid email format")
    @Test
    public void testCreateUserWithoutAtEmail() {
        String email = "vinkotov.at.example.com";

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGenerator.getRegistrationalData(userData);

        Response responseCreateUser = apiCoreRequest.makePostRequest("https://playground.learnqa.ru/api/user", userData);

        Assertions.assertResponseCodeEquals(responseCreateUser, 400);
        Assertions.assertResponseTextEquals(responseCreateUser, "Invalid email format");
    }

    @Description("This negative test checks user registration w/o one of the params.")
    @DisplayName("Test User Registration Not Successfully: Param is missing")
    @ParameterizedTest
    @ValueSource(strings = {"username", "firstName", "lastName", "email", "password"})
    public void testCreateUserWithoutParam(String param) {
        Map<String, String> userData = DataGenerator.getRegistrationalData();
        userData.remove(param);

        Response responseCreateUser = apiCoreRequest.makePostRequest("https://playground.learnqa.ru/api/user", userData);

        Assertions.assertResponseCodeEquals(responseCreateUser, 400);
        Assertions.assertResponseTextEquals(responseCreateUser, "The following required params are missed: " + param);
    }

    @Description("This negative test checks user registration with too short & too long firstName.")
    @DisplayName("Test User Registration Not Successfully: Too short or too long firstName")
    @ParameterizedTest
    @ValueSource(strings = {"short", "long"})
    public void testCreateUserWithBadName(String length) {
        String param = "firstName";
        int len = length.equals("short") ? 1 : 251; // 1 for short, 251 for long
        Map<String, String> userData = DataGenerator.getRegistrationalDataWithNeededLength(param, len);

        Response responseCreateUser = apiCoreRequest.makePostRequest("https://playground.learnqa.ru/api/user", userData);

        Assertions.assertResponseCodeEquals(responseCreateUser, 400);
        Assertions.assertResponseTextEquals(responseCreateUser, "The value of '"+ param +"' field is too " + length);
    }
}
