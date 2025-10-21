package generator;

import com.github.javafaker.Faker;

public class Generator {

    // Генерация номера телефона "+7" + 10 цифр
    public static String generatePhone() {

        Faker faker = new Faker();

        return "+7" + faker.numerify("##########");
    }
}
