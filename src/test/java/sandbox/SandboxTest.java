package sandbox;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class SandboxTest {

    @Test
    public void testRestAssured() {

        Map<String, String> params = new HashMap<>();
        params.put("name", "Max");

        JsonPath response = RestAssured                                // RestAssured - работает по паттерну Builder
                .given()
                .queryParams(params)
                .get("https://playground.learnqa.ru/api/hello")     // Setter паттерна Builder
                .jsonPath();                                          // Executor. Возвращаем ответ от метода Get
        String answer = response.get("answer");
        if (answer == null) {
            System.out.println("The key is absent");
        } else {
            System.out.println(answer);
        }
    }

    @Test
    public void testGetHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3");
        headers.put("Accept-Language", "en-US,en;q=0.9");

        Response response = RestAssured
                .given()
                .headers(headers)
                .redirects()
                .follow(false)
                .when()
                .get("https://playground.learnqa.ru/api/get_303")
                .andReturn();

        response.prettyPrint();

        Headers headersResponse = response.getHeaders();
        System.out.println("Headers:\n\n" + headersResponse);

        String locationHeader = response.getHeader("Location");
        System.out.println("Location Header: " + locationHeader);
    }
}