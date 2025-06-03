package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Epic("Edit Users Cases")
@Feature("Edit Users")
public class UserEditTest extends BaseTestCase {

    @Description("This test checks editing user data after successful registration and login.")
    @DisplayName("Positive Test: Successful User Edit")
    @Test
    public void testEditNewUserTest() {
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationalData();

        JsonPath responseCreateAuth = apiCoreRequest
                .makePostRequest(BASE_URL + "user", userData)
                .jsonPath();

        String userId = responseCreateAuth.getString("id");

        //LOGIN USER
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequest
                .makePostRequest(BASE_URL + "user/login", authData);

        //EDIT USER
        String newName = "Edited Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequest.makePutRequest(
                BASE_URL + "user/" + userId,
                editData,
                this.getHeader(responseGetAuth, "x-csrf-token"),
                this.getCookie(responseGetAuth, "auth_sid")
        );

        //GET NEW USER DATA
        Response responseNewUserData = apiCoreRequest.makeGetRequestWithCookieAndHeader(
                BASE_URL + "user/" + userId,
                this.getHeader(responseGetAuth, "x-csrf-token"),
                this.getCookie(responseGetAuth, "auth_sid")
        );

        //ASSERTIONS
        Assertions.assertResponseCodeEquals(responseEditUser, 200);
        Assertions.assertJsonByName(responseNewUserData, "firstName", newName);
    }

    @Test
    @Description("This test checks editing user email to Invalid Format.")
    @DisplayName("Negative Test: Edit User Email to Invalid Format")
    public void testEditUserInvalidEmail() {
        Map<String, String> userData = DataGenerator.getRegistrationalData();
        JsonPath createResponse = apiCoreRequest.makePostRequest(BASE_URL + "user", userData).jsonPath();
        String userId = createResponse.getString("id");

        Map<String, String> authData = Map.of(
                "email", userData.get("email"),
                "password", userData.get("password")
        );
        Response authResponse = apiCoreRequest.makePostRequest(BASE_URL + "user/login", authData);

        Map<String, String> editData = Map.of("email", "invalidemail.com");

        Response editResponse = apiCoreRequest.makePutRequest(
                BASE_URL + "user/" + userId,
                editData,
                getHeader(authResponse, "x-csrf-token"),
                getCookie(authResponse, "auth_sid")
        );

        Assertions.assertResponseCodeEquals(editResponse, 400);
        Assertions.assertJsonByName(editResponse, "error", "Invalid email format");
    }

    @Test
    @Description("This test checks editing user data without authorization.")
    @DisplayName("Negative Test: Edit User Without Auth")
    public void testEditUserWithoutAuth() {
        Map<String, String> userData = DataGenerator.getRegistrationalData();
        String userId = apiCoreRequest.makePostRequest(BASE_URL + "user", userData)
                .jsonPath().getString("id");

        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", "Unauthorized");

        Response response = apiCoreRequest.makePutRequestWithoutAuth(
                BASE_URL + "user/" + userId,
                editData
        );

        Assertions.assertResponseCodeEquals(response, 400);
        Assertions.assertJsonByName(response, "error", "Auth token not supplied");
    }

    @Test
    @Description("This test checks prohibit editing user data with authorization as another user.")
    @DisplayName("Negative Test: Edit User With Auth of Another User")
    public void testEditUserWithAuthAsAnotherUser() {
        // Create user A
        Map<String, String> userA = DataGenerator.getRegistrationalData();
        String userIdA = apiCoreRequest.makePostRequest(BASE_URL + "user", userA)
                .jsonPath().getString("id");

        // Create and login user B
        Map<String, String> userB = DataGenerator.getRegistrationalData();
        apiCoreRequest.makePostRequest(BASE_URL + "user", userB);

        Map<String, String> authDataB = new HashMap<>();
        authDataB.put("email", userB.get("email"));
        authDataB.put("password", userB.get("password"));

        Response authResponseB = apiCoreRequest.makePostRequest(BASE_URL + "user/login", authDataB);

        // Try to edit user A while logged in as B
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", "Hacker");

        Response response = apiCoreRequest.makePutRequest(
                BASE_URL + "user/" + userIdA,
                editData,
                getHeader(authResponseB, "x-csrf-token"),
                getCookie(authResponseB, "auth_sid")
        );

        Assertions.assertResponseCodeEquals(response, 400);
        Assertions.assertJsonByName(response, "error", "This user can only edit their own data.");

    }

    @Test
    @Description("This test checks fail editing user with too short firstName.")
    @DisplayName("Negative Test: Edit User With Short FirstName")
    public void testEditUserShortFirstName() {
        Map<String, String> userData = DataGenerator.getRegistrationalData();
        JsonPath createResponse = apiCoreRequest.makePostRequest(BASE_URL + "user", userData).jsonPath();
        String userId = createResponse.getString("id");

        Map<String, String> authData = Map.of(
                "email", userData.get("email"),
                "password", userData.get("password")
        );
        Response authResponse = apiCoreRequest.makePostRequest(BASE_URL + "user/login", authData);

        Map<String, String> editData = Map.of("firstName", "A");

        Response editResponse = apiCoreRequest.makePutRequest(
                BASE_URL + "user/" + userId,
                editData,
                getHeader(authResponse, "x-csrf-token"),
                getCookie(authResponse, "auth_sid")
        );

        Assertions.assertResponseCodeEquals(editResponse, 400);
        Assertions.assertJsonByName(editResponse, "error", "The value for field `firstName` is too short");
    }
}
