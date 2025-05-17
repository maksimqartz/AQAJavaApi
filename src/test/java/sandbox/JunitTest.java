package sandbox;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JunitTest {

    @ParameterizedTest //Заменяет @Test, принимает параметры, запускается столько раз, сколько параметров
    @ValueSource(strings = {"", "Max", "John Doe"}) //Набор параметров (3 запусков)
    public void testParametrized(String name) {
        Map<String, String> queryParams = new HashMap<>();

        if (!name.isEmpty()) { //Обработка не пустых имён
            queryParams.put("name", name);
        }

        JsonPath response = RestAssured
                .given()
                .queryParams(queryParams)
                .get("https://playground.learnqa.ru/api/hello")
                .jsonPath();
        String text = response.getString("answer");
        String expectedName = name.isEmpty() ? "someone" : name; //Тернарный оператор (условие ? true : false)
        assertEquals("Hello, " + expectedName, text, "Response text is not correct");
    }

    @Test
    public void testPositive() {
        Response response = RestAssured
                .given()
                .get("https://playground.learnqa.ru/api/map")
                .andReturn();
        assertEquals(200, response.getStatusCode(), "Response code is not 200");
    }

    @Test
    public void testNegative() {
        Response response = RestAssured
                .given()
                .get("https://playground.learnqa.ru/api/mapingABZ")
                .andReturn();
        assertEquals(404, response.getStatusCode(), "Response code is not 200");
    }

}
