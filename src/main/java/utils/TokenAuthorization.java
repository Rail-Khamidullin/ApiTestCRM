package utils;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;
import static data.InitialData.*;
import static io.restassured.RestAssured.given;

public class TokenAuthorization {

    private final static String URL = URL_AUTH_107;

    @Step("Получение токена")
    public static String getToken() {
        Map<String, String> createBody = Map.of("email", REGISTRATION_EMAIL, "password", REGISTRATION_PASSWORD);
        RestAssured.useRelaxedHTTPSValidation();
        return  given()
                .when()
                .body(createBody)
                .contentType(ContentType.JSON)
                .post(URL)
                .then()
                .extract().response().jsonPath().get("token").toString();
    }
}
