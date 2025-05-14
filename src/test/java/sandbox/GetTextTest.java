package sandbox;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class GetTextTest {

    @Test
    public void testGetText() {
        Response response = RestAssured
                .get(ApiEndpoints.BASE_URL+ "/get_text");
        response.prettyPrint();
    }
}
