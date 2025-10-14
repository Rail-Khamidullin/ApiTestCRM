package rest;

import api.UserJSON;
import api.entityRequest.FilterRequest;
import api.entityRequest.SortOption;
import api.entityRequest.entityRequest;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.Collections;

import static io.restassured.RestAssured.given;

public class SupportREST {

    // Endpoint авторизации
    public static final String SIGN_IN_API = "/api/v1/auth/signin";
    // Endpoint получения списка дел
    public static final String MEETING_LIST_API = "/api/v1/meeting/list";
    // Endpoint удаления дела
    public static final String DELETE_MEETING_API = "/api/v1/meeting";
    // Endpoint получения списка Лидов
    public static final String INTEREST_LIST_API = "/api/v1/interest/list";
    // Endpoint удаления Лида
    public static final String DELETE_INTEREST_API = "/api/v1/interest";

    @Step("Получение токена авторизации")
    public String getToken(UserJSON creds) {

        Response response = given()
                .header("Content-type", "application/json")
                .log().all()
                .body(creds)
                .when()
                .post(SIGN_IN_API)
                .then()
                .statusCode(200)  // проверка успешного статус
                .extract()
                .response();

        // Явно используем jsonPath() для извлечения значения
        return response.jsonPath().getString("token");
    }

    // получить айди созданной сущности
    public int getId(String token, String locator) {
        Response response = getList(token, locator);
        return response.path("data[0].id");
    }

    // получить название сущности
    public String getName(String token, String locator) {
        Response response = getList(token, locator);
        return response.path("data[0].name");
    }

    @Step("Получение списка сущностей")
    public Response getList(String token, String locator) {

        entityRequest entityRequest = new entityRequest();
        entityRequest.setPage(1);
        entityRequest.setSize(10);

        FilterRequest filter = new FilterRequest();
        filter.setMistaken(false);
        entityRequest.setFilter(filter);

        SortOption sort = new SortOption();
        sort.setProperty("id");
        sort.setDirection("DESC");
        entityRequest.setSortBy(Collections.singletonList(sort));

        Response response = given()
                .header("Content-type", "application/json")
                .header("authorization", "Bearer "+ token)
                .log().all()
                .body(entityRequest)
                .when()
                .post(locator);
        response.then().statusCode(200);

        return response;
    }

    @Step("Удаление созданной сущности")
    public void deleteEntity(String token, int idEntity, String locator) {
        Response response = given()
                .header("authorization", "Bearer "+ token)
                .log().all()
                .pathParams("id", idEntity)
                .when()
                .delete(locator + "/{id}");

        response.then()
                .log().all()
                .statusCode(200); // проверка статус код
    }
}
