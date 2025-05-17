package homework;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Ex12HeaderCheckTest {

    private static final String BASE_URL = "https://playground.learnqa.ru/api/homework_header";

    private String getHeaderValue(String headerName) {
        return RestAssured
                .given()
                .get(BASE_URL)
                .getHeader(headerName);
    }


    @Test
    public void testHomeworkCookie() {
        String headerName = "x-secret-homework-header";
        String headerValue = getHeaderValue(headerName);
        assertEquals("Some secret value", headerValue, "Unexpected header value");
    }
}
