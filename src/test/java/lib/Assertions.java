package lib;

import io.restassured.response.Response;

import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Assertions {

    /**
     * Asserts that the response contains a specific header with the expected value.
     *
     * @param Response          Объект с ответом сервера
     * @param name              Имя по которому ищем значение в json
     * @param expectedValue     Ожидаемое значение
     */
    public static void assertJsonByName(Response Response, String name, int expectedValue) {
        Response.then().assertThat().body("$", hasKey(name));

        int value = Response.jsonPath().getInt(name);
        assertEquals(expectedValue, value, "Json value is not equal to expected");
    }
}
