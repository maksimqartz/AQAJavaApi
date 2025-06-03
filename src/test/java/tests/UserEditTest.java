package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
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
    @DisplayName("Test User Edit Successfully")
    @Test
    public void testEditNewUserTest() {
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationalData();

        JsonPath responseCreateAuth = apiCoreRequest
                .makePostRequest("https://playground.learnqa.ru/api/user", userData)
                .jsonPath();

        String userId = responseCreateAuth.getString("id");

        //LOGIN USER
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequest
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        //EDIT USER
        String newName = "Edited Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = RestAssured
                .given()
                .header("x-csrf-token", this.getHeader(responseGetAuth, "x-csrf-token"))
                .cookie("auth_sid", this.getCookie(responseGetAuth, "auth_sid"))
                .body(editData)
                .put("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();

        //GET NEW USER DATA
        Response responseNewUserData = RestAssured
                .given()
                .header("x-csrf-token", this.getHeader(responseGetAuth, "x-csrf-token"))
                .cookie("auth_sid", this.getCookie(responseGetAuth, "auth_sid"))
                .get("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();

        //ASSERTIONS
        Assertions.assertResponseCodeEquals(responseEditUser, 200);
        Assertions.assertJsonByName(responseNewUserData, "firstName", newName);
    }
}
