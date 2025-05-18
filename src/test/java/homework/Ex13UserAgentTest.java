package homework;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Ex13UserAgentTest {

    private static final String BASE_URL = "https://playground.learnqa.ru/ajax/api/user_agent_check";


    private static final String ANDROID_AGENT = "Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30";
    private static final String IPAD_AGENT = "Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1";
    private static final String GOOGLEBOT_AGENT = "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)";
    private static final String WIN10_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0";
    private static final String IPHONE_AGENT = "Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1";

    @BeforeEach
    public void setUp() {

    }

    @ParameterizedTest
    @ValueSource(strings = {ANDROID_AGENT, IPAD_AGENT, GOOGLEBOT_AGENT, WIN10_AGENT, IPHONE_AGENT})
    public void testUserAgent(String userAgent) {
        JsonPath response = RestAssured
                .given()
                .header("User-Agent", userAgent)
                .get(BASE_URL)
                .jsonPath();

        String userAgentResponse = response.getString("user_agent");
        String platformResponse = response.getString("platform");
        String browserResponse = response.getString("browser");
        String deviceResponse = response.getString("device");

        assertEquals(userAgent, userAgentResponse,
                "\nUser-Agent header is not present in the response. Actual response: " + userAgentResponse);
       // assertEquals("Test", deviceResponse,
       //         "\nUser-Agent header is not present in the response. Actual response: " + deviceResponse);
       // assertEquals("Test", browserResponse,
       //         "\nUser-Agent header is not present in the response. Actual response: " + browserResponse);
       // assertEquals("Test", platformResponse,
       //         "\nUser-Agent header is not present in the response. Actual response: " + platformResponse);
    }
}
