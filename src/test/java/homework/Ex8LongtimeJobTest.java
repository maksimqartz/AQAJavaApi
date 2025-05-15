package homework;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Ex8LongtimeJobTest {

    @Test
    public void testLongtimeJob() throws InterruptedException {

        // 1. Создаем задачу
        JsonPath responseTaskOpen = RestAssured
                .given()
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();
        String token = responseTaskOpen.getString("token");
        int seconds = responseTaskOpen.getInt("seconds");

        Assertions.assertNotNull(token, "Token is null");
        System.out.println("\nToken: " + token + "\nSeconds: " + seconds);

        // 2. Делаем запрос с token ДО того, как задача готова
        JsonPath responseTaskNotReady = RestAssured
                .given()
                .get("https://playground.learnqa.ru/ajax/api/longtime_job?token=" + token)
                .jsonPath();
        String statusTask = responseTaskNotReady.getString("status");

        Assertions.assertEquals("Job is NOT ready", statusTask, "Status is not correct");
        System.out.println("\nResponse before waiting: " + statusTask);

        // 3. Ждем нужное количество секунд
        Thread.sleep(seconds * 1000L);

        // 4. Делаем запрос c token ПОСЛЕ того, как задача готова
        JsonPath responseTaskReady = RestAssured
                .given()
                .get("https://playground.learnqa.ru/ajax/api/longtime_job?token=" + token)
                .jsonPath();
        String statusTaskReady = responseTaskReady.getString("status");
        String resultTaskReady = responseTaskReady.getString("result");

        Assertions.assertEquals("Job is ready", statusTaskReady, "Status is not correct");
        Assertions.assertNotNull(resultTaskReady, "Result is null");
        System.out.println("\nResponse after waiting: " + statusTaskReady + "\nResult: " + resultTaskReady);
    }
}
