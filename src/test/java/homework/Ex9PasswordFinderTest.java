package homework;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class Ex9PasswordFinderTest {

    private static final String BASE_URL = "https://playground.learnqa.ru/ajax/api/";
    private static final String GET_AUTH_ENDPOINT = "get_secret_password_homework";
    private static final String CHECK_AUTH_ENDPOINT = "check_auth_cookie";

    private static final List<String> popularPasswords = Arrays.asList(
            "123456", "123456789", "qwerty", "password", "1234567",
            "12345678", "12345", "iloveyou", "111111", "123123",
            "abc123", "qwerty123", "1q2w3e4r", "admin", "qwertyuiop",
            "654321", "555555", "lovely", "7777777", "welcome",
            "888888", "princess", "dragon", "password1", "123qwe");

    private String getAuthCookie(Map<String, String> loginData) {
        return RestAssured
                .given()
                .body(loginData)
                .when()
                .post(BASE_URL + GET_AUTH_ENDPOINT)
                .getCookie("auth_cookie");
    }

    private Response checkAuthCookie(String token) {
        return RestAssured
                .given()
                .cookie("auth_cookie", token)
                .when()
                .get(BASE_URL + CHECK_AUTH_ENDPOINT)
                .andReturn();
    }

    @Test
    public void testLoginAttempts() {

        Map<String, String> data = new HashMap<>();
        data.put("login", "super_admin");

        for (String password : popularPasswords) {
            System.out.println("Trying password: " + password);
            data.put("password", password);

            String token = getAuthCookie(data);
            Assertions.assertNotNull(token, "Token is null");

            Response loginResponse = checkAuthCookie(token);
            loginResponse.print();
            String authorizeResponse = loginResponse.asString();

            if (Objects.equals(authorizeResponse, "You are authorized")) {
                System.out.println("Password found: " + password);
                Assertions.assertEquals("You are authorized", authorizeResponse, "Authorization failed");
                break;
            }
        }
    }
}
