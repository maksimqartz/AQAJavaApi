package homework;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Ex10StringLengthTest {

    @ParameterizedTest
    @ValueSource(strings = {"", "Abba", "Test This One", "Hello, world. This is a test",
            "Если текст длиннее 15 символов, то тест должен проходить успешно. Иначе падать с ошибкой."})
    public void testStringLength(String text) {
        assertTrue(text.length() > 15,
                "\nText length is less than or equal to 15 characters. Actual length: " + text.length());

    }
}
