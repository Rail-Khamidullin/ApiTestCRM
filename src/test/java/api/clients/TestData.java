package api.clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import generator.Generator;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static api.clients.BaseApiTestConfig.BASE_URI;
import static io.restassured.RestAssured.given;

public class TestData {

    String df = REGISTRATION_EMAIL;

    /// === Создание Лида ===

    @Step("Создание Лида")
    public Response createInterest() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode testData = mapper.readValue(new File("src/test/resources/json/create_interest_request.json"), ObjectNode.class);

        // Получаем узел массива contactsInfoList
        ArrayNode contactsInfoList = (ArrayNode) testData.get("extContact").get("contactsInfoList");
        // Получаем первый элемент массива и приводим к ObjectNode
        ObjectNode firstContact = (ObjectNode) contactsInfoList.get(0);
        // firstContact.put("name", UUID.randomUUID() + "@mail.ru");
        firstContact.put("name", Generator.generatePhone());
        Response response =
                given()
                        .header("Authorization", "Bearer " + BaseApiTestConfig.TOKEN)
                        .log().all()
                        .when()
                        .contentType(ContentType.JSON)
                        .body(testData.toString())
                        .post(BASE_URI + "/api/v1/interest");

        return response;
    }

    @Step("Получение успешного ответа и id Лида")
    public String getInterestId(Response interest) {
        return interest.then().assertThat()
                .statusCode(200)
                .extract().response().jsonPath().getString("id");
    }

    /// === Перевод Лида в 'Интерес' ===

    @Step("Перевод лида в статус интерес")
    public Response convertLeadToInterest(String interestId) {
        Response response =
                given()
                        .header("Authorization", "Bearer " + BaseApiTestConfig.TOKEN)
                        .when()
                        .contentType(ContentType.JSON)
                        .post(BASE_URI + "/api/v1/interest/" + interestId + "/interest");

        return response;
    }

    @Step("Получение успешного ответа и id Контакта")
    public int getContactId(Response interest) {
        return interest.then().assertThat()
                .statusCode(200)
                .extract().response().jsonPath().get("contact.id");
    }

    /// === Создание реквизитов ===

    @Step("Создание реквизитов контакту")
    public void createContactDetails(int contactId) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode testData = mapper.readValue(
                new File("src/test/resources/json/create_contact_details.json"),
                ObjectNode.class);

        testData.put("contactId", contactId);

        given()
                .header("Authorization", "Bearer " + BaseApiTestConfig.TOKEN)
                .log().all()
                .when()
                .contentType(ContentType.JSON)
                .body(testData.toString())
                .post(BASE_URI + "/api/v1/requisites")
                .then().log().all()
                .statusCode(200);
    }

    /// === Добавление в избраное ОН ===

    @Step("Добавление в избраное ОН")
    public void addToFavorites(int objectId, String interestId) throws IOException {

        Map<String, Integer> createBody = Map.of("objectTypeId", 1, "objectId", objectId);
        List<Map<String, Integer>> requestBody = Arrays.asList(createBody);
        given()
                .header("Authorization", "Bearer " + BaseApiTestConfig.TOKEN)
                .when()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .patch(BASE_URI + "/api/v1/interest/" + interestId + "/likes")
                .then().log().all()
                .statusCode(200);
    }

    /// === Создание сделки ===

    @Step("Создание сделки")
    public Response createDeal(int objectId, String interestId) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode testData = mapper.readValue(new File("src/test/resources/json/create_deal.json"), ObjectNode.class);

        testData.put("interestId", interestId);
        testData.put("objectId", objectId);
        Response dealId =
                given().log().body()
                        .header("Authorization", "Bearer " + BaseApiTestConfig.TOKEN)
                        .log().all()
                        .when()
                        .contentType(ContentType.JSON)
                        .body(testData.toString())
                        .post(BASE_URI + "/api/v1/deal");
        return dealId;
    }

    @Step("Получение успешного ответа и id Сделки")
    public int getDealId(Response deal) {
        return deal.then().assertThat()
                .statusCode(200)
                .extract().response().jsonPath().get("id");
    }

    /// === Заполнение полей сделки для перевода в статус "Оплачено" ===

    @Step("Заполнение полей сделки для перевода в статус 'Оплачено'")
    public Response updateAllFields(int dealId) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode testData = mapper.readValue(new File("src/test/resources/json/update_all_fields.json"), ObjectNode.class);

        Response objectDiscountAmount =
                given().log().body()
                        .header("Authorization", "Bearer " + BaseApiTestConfig.TOKEN)
                        .when()
                        .contentType(ContentType.JSON)
                        .body(testData.toString())
                        .put(BASE_URI + "/api/v1/deal/" + dealId);
        return objectDiscountAmount;
    }

    @Step("Получение успешного ответа и float на заполнение полей")
    public float getObjectDiscount(Response response) {
        return response.then().assertThat()
                .statusCode(200)
                .extract().response().jsonPath().get("realEstateObject.objectDiscountAmount");
    }

    /// === Перевод сделки в статус "Подготовка" ===

    @Step("Перевод сделки в статус 'Подготовка'")
    public void moveDealToPreparation(int dealId) {

        given().log().body()
                .header("Authorization", "Bearer " + BaseApiTestConfig.TOKEN)
                .when()
                .contentType(ContentType.JSON)
                .post(BASE_URI + "/api/v1/deal/" + dealId + "/prepare")
                .then().log().all()
                .statusCode(200);
    }

    /// === Перевод сделки в статус "Согласование" ===

    @Step("Перевод сделки в статус 'Согласование'")
    public void moveDealToApproval(int dealId) {
        given().log().body()
                .header("Authorization", "Bearer " + BaseApiTestConfig.TOKEN)
                .when()
                .contentType(ContentType.JSON)
                .post(BASE_URI + "/api/v1/deal/" + dealId + "/coordinate")
                .then().log().all()
                .statusCode(200);
    }

    /// === Перевод сделки в статус "Проверка договора" ===

    @Step("Перевод сделки в статус 'Проверка договора'")
    public void moveDealToContractReview(int dealId) {
        given().log().body()
                .header("Authorization", "Bearer " + BaseApiTestConfig.TOKEN)
                .when()
                .contentType(ContentType.JSON)
                .post(BASE_URI + "/api/v1/deal/" + dealId + "/check_ddu")
                .then().log().all()
                .statusCode(200);
    }

    /// === Сгенерировать первичный документ ===

    @Step("Сгенерировать первичный документ")
    public void generatePrimaryDocument(int dealId) {
        File file = new File("src/test/resources/fileToSend/deal-34719.docx");

        Map<String, Integer> createBody = Map.of("documentTypeId", 3, "documentStageId", 2, "templateId", 12);
        given().log().all()
                .header("Authorization", "Bearer " + BaseApiTestConfig.TOKEN)
                .multiPart("file", file)
                .multiPart("documentTypeId", 3)
                .multiPart("documentStageId", 2)
                .multiPart("templateId", 12)
                .when()
                .post(BASE_URI + "/api/v1/deal/" + dealId + "/documents/save")
                .then().log().all()
                .statusCode(200);
    }

    /// === Перевод сделки в статус "Подписание" ===

    @Step("Перевод сделки в статус 'Подписание'")
    public void moveDealToSigning(int dealId) {
        given().log().body()
                .header("Authorization", "Bearer " + BaseApiTestConfig.TOKEN)
                .when()
                .contentType(ContentType.JSON)
                .post(BASE_URI + "/api/v1/deal/" + dealId + "/signing")
                .then().log().all()
                .statusCode(200);
    }

    /// === Перевод сделки в статус "Подписан" ===

    @Step("Перевод сделки в статус 'Подписан'")
    public void moveDealToSigned(int dealId) {
        given().log().body()
                .header("Authorization", "Bearer " + BaseApiTestConfig.TOKEN)
                .when()
                .contentType(ContentType.JSON)
                .post(BASE_URI + "/api/v1/deal/" + dealId + "/sign")
                .then().log().all()
                .statusCode(200);
    }

    /// === Перевод сделки в статус "Готов к регистрации" ===

    @Step("Перевод сделки в статус 'Готов к регистрации'")
    public void moveDealToReadyForRegistration(int dealId) {
        given().log().body()
                .header("Authorization", "Bearer " + BaseApiTestConfig.TOKEN)
                .when()
                .contentType(ContentType.JSON)
                .post(BASE_URI + "/api/v1/deal/" + dealId + "/readyForRegistration")
                .then().log().all()
                .statusCode(200);
    }

    /// === Перевод сделки в статус "Отправлен на регистрацию" ===

    @Step("Перевод сделки в статус 'Отправлен на регистрацию'")
    public void moveDealToSentForRegistration(int dealId) {
        given().log().body()
                .header("Authorization", "Bearer " + BaseApiTestConfig.TOKEN)
                .when()
                .contentType(ContentType.JSON)
                .post(BASE_URI + "/api/v1/deal/" + dealId + "/sentForRegistration")
                .then().log().all()
                .statusCode(200);
    }

    /// === Загрузить документ "Выписка из ЕГРН" ===

    @Step("Загрузить документ 'Выписка из ЕГРН'")
    public void uploadEGRNDocument(int contactId, int dealId) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode testData = mapper.readValue(new File("src/test/resources/json/upload_EGRN_document.json"), ObjectNode.class);

        testData.put("contactId", contactId);

        given().log().body()
                .header("Authorization", "Bearer " + BaseApiTestConfig.TOKEN)
                .when()
                .contentType(ContentType.JSON)
                .body(testData.toString())
                .post(BASE_URI + "/api/v1/deal/" + dealId + "/documents")
                .then().log().all()
                .statusCode(200);
    }

    /// === Перевод сделки в статус "Зарегистрирован" ===

    @Step("Перевод сделки в статус 'Зарегистрирован'")
    public void moveDealToRegistered(int dealId) {
        given().log().body()
                .header("Authorization", "Bearer " + BaseApiTestConfig.TOKEN)
                .when()
                .contentType(ContentType.JSON)
                .post(BASE_URI + "/api/v1/deal/" + dealId + "/registered")
                .then().log().all()
                .statusCode(200);
    }

    /// === Перевод сделки в статус "Контроль оплаты по графику" ===

    @Step("Перевод сделки в статус 'Контроль оплаты по графику'")
    public void moveDealToPaymentScheduleControl(int dealId) {

        given().log().body()
                .header("Authorization", "Bearer " + BaseApiTestConfig.TOKEN)
                .when()
                .contentType(ContentType.JSON)
                .post(BASE_URI + "/api/v1/deal/" + dealId + "/control")
                .then().log().all()
                .statusCode(200);
    }

    /// === Пополнение Эскроу-счета ===

    @Step("Пополнение Эскроу-счета")
    public void topUpEscrowAccount(int dealId, float objectDiscountAmount) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode testData = mapper.readValue(new File("src/test/resources/json/top_up_escrow_account.json"), ObjectNode.class);

        testData.put("amount", objectDiscountAmount);

        given().log().body()
                .header("Authorization", "Bearer " + BaseApiTestConfig.TOKEN)
                .when()
                .contentType(ContentType.JSON)
                .body(testData.toString())
                .post(BASE_URI + "/api/v1/deal/" + dealId + "/escrow-account-state")
                .then().log().all()
                .statusCode(200);
    }
}
