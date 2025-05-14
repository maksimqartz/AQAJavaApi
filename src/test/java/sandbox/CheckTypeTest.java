package sandbox;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class CheckTypeTest {

    @Test
    public void testGetCheckType(){
        Map<String, String> params = new HashMap<>();
        params.put("name", "Max");
        params.put("city", "Bishkek");

        Response response = RestAssured
                .given()
                .queryParams(params)
                .get("https://playground.learnqa.ru/api/check_type")
                .andReturn();
        response.print();
        int statusCode = response.getStatusCode();
        System.out.printf("STATUS CODE: %s%n", statusCode);
    }

    @Test
    public void testGet500CheckType(){
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/get_500")
                .andReturn();
        int statusCode = response.getStatusCode();
        System.out.printf("STATUS CODE: %s%n", statusCode);
        Assertions.assertEquals(500, statusCode, "Status code is not 500");
    }

    @Test
    public void testGetRedirect200CheckType(){
        Response response = RestAssured
                .given()
                .redirects()
                .follow(true)
                .when()
                .get("https://playground.learnqa.ru/api/get_303")
                .andReturn();
        int statusCode = response.getStatusCode();
        System.out.printf("STATUS CODE: %s%n", statusCode);
        Assertions.assertEquals(200, statusCode, "Status code is not 303");
    }

    @Test
    public void testPostCheckType(){
        Map<String, Object> body = new HashMap<>();
        body.put("name", "New Kolya");
        body.put("city", "Cairo");

        Response response = RestAssured
                .given()
                .body(body)
                .post("https://playground.learnqa.ru/api/check_type")
                .andReturn();
        response.print();
    }
}
