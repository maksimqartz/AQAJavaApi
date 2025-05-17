package homework;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Ex11CookieCheckTest {

    private static final String BASE_URL = "https://playground.learnqa.ru/api/homework_cookie";

    private String getCookieValue(String cookieName) {
        return RestAssured
                .given()
                .get(BASE_URL)
                .getCookie(cookieName);
    }


    @Test
    public void testHomeworkCookie() {
        String cookieName = "HomeWork";
        String cookieValue = getCookieValue(cookieName);
        assertEquals("hw_value", cookieValue, "Unexpected cookie value");
    }
}

