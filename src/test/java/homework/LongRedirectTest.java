package homework;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LongRedirectTest {

    @Test
    public void testLongRedirect() {
        String initialUrl = "https://playground.learnqa.ru/api/long_redirect";
        String redirectUrl = "https://playground.learnqa.ru/";

        Response responseLongRedirect = RestAssured
                .given()
                .redirects()
                .follow(false)
                .when()
                .get(initialUrl)
                .andReturn();

        System.out.println("\nRedirect Location: ");
        String responseHeader = responseLongRedirect.getHeader("Location");
        System.out.println(responseHeader);

        Assertions.assertEquals(redirectUrl, responseHeader, "Location header is not correct");
    }
}
