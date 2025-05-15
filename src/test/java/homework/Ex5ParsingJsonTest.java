package homework;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

public class Ex5ParsingJsonTest {

    @Test
    public void testParsingJson() {

        JsonPath responseJson = RestAssured
                .given()
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();
        String message = responseJson.getString("messages[1]");
        System.out.println("Second message: " + message);
    }
}
