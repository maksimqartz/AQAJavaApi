package homework;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Ex13UserAgentTest {

    private static final String BASE_URL = "https://playground.learnqa.ru/ajax/api/user_agent_check";

    static Stream<UserAgentPojo> provideTestData() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = Ex13UserAgentTest.class.getClassLoader().getResourceAsStream("user_agents_data.json");
        List<UserAgentPojo> cases = mapper.readValue(is, new TypeReference<>() {});
        return cases.stream();
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void testUserAgentDetection(UserAgentPojo userAgentTestCase) {
        JsonPath response = RestAssured
                .given()
                .header("User-Agent", userAgentTestCase.getUserAgent())
                .get(BASE_URL)
                .jsonPath();

        String actualPlatform = response.getString("platform");
        String actualBrowser = response.getString("browser");
        String actualDevice = response.getString("device");

        if (!actualPlatform.equals(userAgentTestCase.getExpectedPlatform()) ||
                !actualBrowser.equals(userAgentTestCase.getExpectedBrowser()) ||
                !actualDevice.equals(userAgentTestCase.getExpectedDevice())) {

            System.out.printf("❌ FAILED for User-Agent: %s%n", userAgentTestCase.getUserAgent());
            System.out.printf("Expected: %s / %s / %s%n", userAgentTestCase.getExpectedPlatform(), userAgentTestCase.getExpectedBrowser(), userAgentTestCase.getExpectedDevice());
            System.out.printf("Actual:   %s / %s / %s%n%n", actualPlatform, actualBrowser, actualDevice);
        }

        assertEquals(userAgentTestCase.getExpectedPlatform(), actualPlatform, "Platform mismatch");
        assertEquals(userAgentTestCase.getExpectedBrowser(), actualBrowser, "Browser mismatch");
        assertEquals(userAgentTestCase.getExpectedDevice(), actualDevice, "Device mismatch");
    }
}
