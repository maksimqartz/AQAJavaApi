package lib;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DataGenerator {
    public static String getRandomEmail() {
    String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    return "learnqa-" + timestamp + "@example.com";
    }

    public static Map<String, String> getRegistrationalData() {
        Map<String, String> userData = new HashMap<>();
        userData.put("email", DataGenerator.getRandomEmail());
        userData.put("password", "123");
        userData.put("username", "learnqa");
        userData.put("firstName", "LearnQA");
        userData.put("lastName", "LearnQA");

        return userData;
    }

    public static Map<String, String> getRegistrationalData(Map<String, String> nonDefaultData) {
        Map<String, String> defaultData = DataGenerator.getRegistrationalData();

        Map<String, String> userData = new HashMap<>(defaultData);
        String[] keys = {"email", "password", "username", "firstName", "lastName"};
        for (String key : keys) {
            if (nonDefaultData.containsKey(key)) {
                userData.put(key, nonDefaultData.get(key));
            } else {
                userData.put(key, defaultData.get(key));
            }
        }
        return userData;
    }
}
