package lib;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Assertions {

    /**
     * Asserts that the response contains a specific header with the expected value.
     *
     * @param Response      Объект с ответом сервера
     * @param name          Имя по которому ищем значение в json
     * @param expectedValue Ожидаемое значение
     */
    @Step("Assert that JSON response contains field '{name}' with int value '{expectedValue}'")
    public static void assertJsonByName(Response Response, String name, int expectedValue) {
        Response.then().assertThat().body("$", hasKey(name));

        int value = Response.jsonPath().getInt(name);
        assertEquals(expectedValue, value, "Json value is not equal to expected");
    }

    @Step("Assert that JSON response contains field '{name}' with String value '{expectedValue}'")
    public static void assertJsonByName(Response Response, String name, String expectedValue) {
        Response.then().assertThat().body("$", hasKey(name));

        String value = Response.jsonPath().getString(name);
        assertEquals(expectedValue, value, "Json value is not equal to expected");
    }

    @Step("Assert that '{Response}' text is equal to '{expectedText}'")
    public static void assertResponseTextEquals(Response Response, String expectedText) {
        assertEquals(expectedText, Response.asString(), "Response text is not equal to expected");
    }

    @Step("Assert that response code is equal to {expectedStatusCode}")
    public static void assertResponseCodeEquals(Response Response, int expectedStatusCode) {
        assertEquals(expectedStatusCode, Response.statusCode(), "Response status code is not equal to expected");
    }

    @Step("Assert that response code is not equal to {unexpectedStatusCode}")
    public static void assertJsonHasField(Response Response, String expectedFieldName) {
        Response.then().assertThat().body("$", hasKey(expectedFieldName));
    }

    @Step("Assert that response JSON has fields: {expectedFieldNames}")
    public static void assertJsonHasFields(Response Response, String[] expectedFieldNames) {
        for (String expectedFieldName : expectedFieldNames) {
            Assertions.assertJsonHasField(Response, expectedFieldName);
        }
    }

    @Step("Assert that response JSON does not have field: {unexpectedFieldName}")
    public static void assertJsonHasNoField(Response Response, String unexpectedFieldName) {
        Response.then().assertThat().body("$", not(hasKey(unexpectedFieldName)));
    }

}
