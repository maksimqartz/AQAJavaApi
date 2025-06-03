package lib;

import io.restassured.http.Headers;
import io.restassured.response.Response;

import java.util.Map;

import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class BaseTestCase {

    protected final ApiCoreRequest apiCoreRequest = new ApiCoreRequest();

    // В метод передаем headers
    protected String getHeader(Response Response, String name) {
        Headers headers = Response.getHeaders();

        // Проверяем, что в headers есть нужный нам header, и возвращаем его
        assertTrue(headers.hasHeaderWithName(name),
                "There is no header with name: " + name);
        return headers.getValue(name);
    }

    // В метод передаем cookies
    protected String getCookie(Response Response, String name) {
        Map<String, String> cookies = Response.getCookies();

        // Проверяем, что в cookies есть нужный нам cookie, и возвращаем его
        assertTrue(cookies.containsKey(name),
                "There is no cookie with name: " + name);
        return cookies.get(name);
    }

    // В метод передаем json
    protected int getIntFromJson(Response Response, String name) {
        // Проверяем, что в корне json есть нужный нам элемент, и возвращаем его
        Response.then().assertThat().body("$", hasKey(name));
        return Response.jsonPath().getInt(name);
    }
}
