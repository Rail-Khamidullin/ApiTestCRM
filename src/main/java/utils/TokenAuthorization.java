package utils;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class TokenAuthorization {

    private final static String URL_AUTH_107 = "https://preprod-crm.sbercity.ru/api/v1/auth/signin";

    @Step("Получение токена")
    public static String getToken() {
        Map<String, String> createBody = Map.of("email", "admin@admin.ru", "password", "GpDW5Z?mlJDY");
        RestAssured.useRelaxedHTTPSValidation();
        return  given()
                .when()
                .body(createBody)
                .contentType(ContentType.JSON)
                .post(URL_AUTH_107)
                .then()
                .extract().response().jsonPath().get("token").toString();
    }
}
