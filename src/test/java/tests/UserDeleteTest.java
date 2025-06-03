package tests;

import io.qameta.allure.*;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.Map;

@Epic("User Deletion Cases")
@Feature("Delete User")
public class UserDeleteTest extends BaseTestCase {

    @Test
    @Description("This test checks successful deletion of a user created by the test")
    @DisplayName("Positive test: Create, Delete and Check user")
    public void testCreateDeleteUser() {
        // Create User
        Map<String, String> userData = DataGenerator.getRegistrationalData();
        Response createResponse = apiCoreRequest.makePostRequest("https://playground.learnqa.ru/api/user/", userData);
        int userId = this.getIntFromJson(createResponse, "id");

        // Login
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response loginResponse = apiCoreRequest.makePostRequest("https://playground.learnqa.ru/api/user/login", authData);
        String token = this.getHeader(loginResponse, "x-csrf-token");
        String cookie = this.getCookie(loginResponse, "auth_sid");

        // Delete
        Response deleteResponse = apiCoreRequest.makeDeleteRequest(
                "https://playground.learnqa.ru/api/user/" + userId,
                token,
                cookie
        );

        Assertions.assertResponseCodeEquals(deleteResponse, 200);

        // Check User deleted
        Response getResponse = apiCoreRequest.makeGetRequestWithCookieAndHeader(
                "https://playground.learnqa.ru/api/user/" + userId,
                token,
                cookie
        );

        Assertions.assertResponseCodeEquals(getResponse, 404);
        Assertions.assertResponseTextEquals(getResponse, "User not found");
    }

    @Test
    @Description("This test checks that user with ID=2 cannot be deleted (protected user)")
    @DisplayName("Negative test: Try to delete user with ID=2")
    public void testDeleteProtectedUser() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response loginResponse = apiCoreRequest.makePostRequest("https://playground.learnqa.ru/api/user/login", authData);
        String token = this.getHeader(loginResponse, "x-csrf-token");
        String cookie = this.getCookie(loginResponse, "auth_sid");

        Response deleteResponse = apiCoreRequest.makeDeleteRequest(
                "https://playground.learnqa.ru/api/user/2",
                token,
                cookie
        );

        Assertions.assertResponseCodeEquals(deleteResponse, 400);
        Assertions.assertJsonByName(deleteResponse, "error", "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");
    }

    @Test
    @Description("This test checks that a user cannot be deleted from another user's account")
    @DisplayName("Negative test: Try to delete user as another user")
    public void testDeleteUserAsAnotherUser() {
        // Create User 1
        Map<String, String> user1Data = DataGenerator.getRegistrationalData();
        Response createUser1 = apiCoreRequest.makePostRequest("https://playground.learnqa.ru/api/user/", user1Data);
        int user1Id = this.getIntFromJson(createUser1, "id");

        // Create User 2
        Map<String, String> user2Data = DataGenerator.getRegistrationalData();
        apiCoreRequest.makePostRequest("https://playground.learnqa.ru/api/user/", user2Data);

        // Login as User 2
        Map<String, String> authData = new HashMap<>();
        authData.put("email", user2Data.get("email"));
        authData.put("password", user2Data.get("password"));

        Response loginUser2 = apiCoreRequest.makePostRequest("https://playground.learnqa.ru/api/user/login", authData);
        String token2 = this.getHeader(loginUser2, "x-csrf-token");
        String cookie2 = this.getCookie(loginUser2, "auth_sid");

        // Attempt to delete User 1 while logged in as User 2
        Response deleteResponse = apiCoreRequest.makeDeleteRequest(
                "https://playground.learnqa.ru/api/user/" + user1Id,
                token2,
                cookie2
        );

        Assertions.assertResponseCodeEquals(deleteResponse, 400);
    }
}
