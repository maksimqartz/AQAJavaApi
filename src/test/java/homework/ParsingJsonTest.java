package homework;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class ParsingJsonTest {

    @Test
    public void testParsingJson() {

        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .andReturn();
        response.prettyPrint();
    }
}
