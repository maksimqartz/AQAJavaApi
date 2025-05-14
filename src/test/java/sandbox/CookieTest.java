package sandbox;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class CookieTest {

    @Test
    public void testCookie() {
        Map<String, String> data = new HashMap<>();
        data.put("login", "secret_login");
        data.put("password", "secret_pass");

        Response responseGetCookie = RestAssured
                .given()
                .body(data)
                .when()
                .post(ApiEndpoints.BASE_URL + "/get_auth_cookie")
                .andReturn();

        String responseCookie = responseGetCookie.getCookie("auth_cookie");

        System.out.println("\nResponse: " + responseGetCookie.prettyPrint());

        System.out.println("\nHeaders: ");
        Headers responseHeaders = responseGetCookie.getHeaders();
        System.out.println(responseHeaders);

        System.out.println("\nCookies: ");
        String responseCookies = responseGetCookie.getCookie("auth_cookie");
        System.out.println(responseCookies);

        Map<String, String> cookies = new HashMap<>();
        if(responseCookie != null) {
            cookies.put("auth_cookie", responseCookie);
        }

        Response responseCheckCookie = RestAssured
                .given()
                .body(data)
                .cookies(cookies)
                .when()
                .get(ApiEndpoints.BASE_URL + "/check_auth_cookie")
                .andReturn();

        responseCheckCookie.print();
    }
}
