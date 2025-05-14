package homework;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class Ex7LongRedirectTest {

    @Test
    public void testLongRedirect() {
        String initialUrl = "https://playground.learnqa.ru/api/long_redirect";
        ArrayList<String> redirectUrls = new ArrayList<>();
        redirectUrls.add(initialUrl);

        for (int i = 0; i < redirectUrls.size(); i++) {
            String currentUrl = redirectUrls.get(i);
            Response responseLongRedirect = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .when()
                    .get(currentUrl)
                    .andReturn();
            int statusCode = responseLongRedirect.getStatusCode();
            String responseHeader = responseLongRedirect.getHeader("Location");

            if (responseHeader == null) {
                System.out.println("\n\nNo more redirects!!!");
                System.out.printf("Status code: %d / Final URL: %s", statusCode, currentUrl);
                Assertions.assertEquals(200, statusCode, "Status code is not 200");
                break;
            } else {
                System.out.printf("\nStatus code: %d\nRedirect Location: %s", statusCode, responseHeader);
                redirectUrls.add(responseHeader);
            }
        }
        System.out.printf("\nNumber of redirects: %s", redirectUrls.size()-1);
    }
}
