package api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import api.clients.BaseApiTestConfig;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class TestApi extends BaseApiTestConfig {
    private String interestId;
    private int contactId;
    private int objectId;
    private int dealId;
    private float objectDiscountAmount;
    private int phone;

    @Test
    @DisplayName("Создание Лида, перевод лида в интерес, создание сделки, перевод сделки в статус Оплачено")
    public void createLeadAndContactWithDealSetPaidStatus() throws IOException {
        phone = 1100000;
        objectId = 3567;
        for (int i = 0; i < 1; i++) {
            createInterest();                       // Создание Лида
            convertLeadToInterest();                // Перевод лида в статус интерес
            createContactDetails();                 // Создание реквизитов контакту
            addToFavorites();                       // Добавление в избраное ОН
            createDeal();                           // Создание сделки
            updateAllFields();                      // Заполнение всех полей, чтобы сделку перевести в оплачено
            moveDealToPreparation();                // Перевод сделки в статус Подготовка
            moveDealToApproval();                   // Перевод сделки в статус Согласование
            moveDealToContractReview();             // Перевод сделки в статус Проверка договора
            generatePrimaryDocument();              // Сгенерировать первичный документ
            moveDealToSigning();                    // Перевод сделки в статус Подписание
            moveDealToSigned();                     // Перевод сделки в статус Подписан
            moveDealToReadyForRegistration();       // Перевод сделки в статус Готов к регистрации
            moveDealToSentForRegistration();        // Перевод сделки в статус Отправлен на регистрацию
            uploadEGRNDocument();                   // Загрузить документ выписка из ЕГРН
            moveDealToRegistered();                 // Перевод сделки в статус Зарегистрирован
            moveDealToPaymentScheduleControl();     // Перевод сделки в статус Контроль оплаты по графику
            topUpEscrowAccount();                   // Пополнение Эскроу-счета
            objectId++;
            phone++;
        }

    }

    // Создаие Лида
    private void createInterest() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode testData = mapper.readValue(new File("src/test/resources/json/create_interest_request.json"), ObjectNode.class);

        // Получаем узел массива contactsInfoList
        ArrayNode contactsInfoList = (ArrayNode) testData.get("extContact").get("contactsInfoList");
        // Получаем первый элемент массива и приводим к ObjectNode
        ObjectNode firstContact = (ObjectNode) contactsInfoList.get(0);
        // firstContact.put("name", UUID.randomUUID() + "@mail.ru");
        firstContact.put("name", "+7900" + phone);
        interestId =
                given()
                        .header("Authorization", "Bearer " + BaseApiTestConfig.TOKEN)
                        .when()
                        .contentType(ContentType.JSON)
                        .body(testData.toString())
                        .post(BASE_URI + "/api/v1/interest")
                        .then().log().all()
                        .statusCode(200)
                        .extract().response().jsonPath().get("id").toString();
    }

    // Перевод лида в статус интерес
    private void convertLeadToInterest() {
        contactId =
                given()
                        .header("Authorization", "Bearer " + BaseApiTestConfig.TOKEN)
                        .when()
                        .contentType(ContentType.JSON)
                        .post(BASE_URI + "/api/v1/interest/" + interestId + "/interest")
                        .then().log().all()
                        .statusCode(200)
                        .extract().response().jsonPath().get("contact.id");
    }

    // Создание реквизитов контакту
    private void createContactDetails() throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode testData = mapper.readValue(new File("src/test/resources/json/create_contact_details.json"), ObjectNode.class);

        testData.put("contactId", contactId);

        given()
                .header("Authorization", "Bearer " + BaseApiTestConfig.TOKEN)
                .when()
                .contentType(ContentType.JSON)
                .body(testData.toString())
                .post(BASE_URI + "/api/v1/requisites")
                .then().log().all()
                .statusCode(200)
                .extract().response();
    }

    // Добавление в избраное ОН
    private void addToFavorites() throws IOException {

        Map<String, Integer> createBody = Map.of("objectTypeId", 1, "objectId", objectId);
        List<Map<String, Integer>> requestBody = Arrays.asList(createBody);
        given()
                .header("Authorization", "Bearer " + BaseApiTestConfig.TOKEN)
                .when()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .patch(BASE_URI + "/api/v1/interest/" + interestId + "/likes")
                .then().log().all()
                .statusCode(200)
                .extract().response();

    }

    // Создание сделки
    private void createDeal() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode testData = mapper.readValue(new File("src/test/resources/json/create_deal.json"), ObjectNode.class);

        testData.put("interestId", interestId);
        testData.put("objectId", objectId);
        dealId =
                given().log().body()
                        .header("Authorization", "Bearer " + BaseApiTestConfig.TOKEN)
                        .when()
                        .contentType(ContentType.JSON)
                        .body(testData.toString())
                        .post(BASE_URI + "/api/v1/deal")
                        .then().log().all()
                        .statusCode(200)
                        .extract().response().jsonPath().get("id");
    }

    // Заполнение всех полей, чтобы сделку перевести в оплачено
    private void updateAllFields() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode testData = mapper.readValue(new File("src/test/resources/json/update_all_fields.json"), ObjectNode.class);

        objectDiscountAmount =
                given().log().body()
                        .header("Authorization", "Bearer " + BaseApiTestConfig.TOKEN)
                        .when()
                        .contentType(ContentType.JSON)
                        .body(testData.toString())
                        .put(BASE_URI + "/api/v1/deal/" + dealId)
                        .then().log().all()
                        .statusCode(200)
                        .extract().response().jsonPath().get("realEstateObject.objectDiscountAmount");
    }

    // Перевод сделки в статус Подготовка
    private void moveDealToPreparation() {

        given().log().body()
                .header("Authorization", "Bearer " + BaseApiTestConfig.TOKEN)
                .when()
                .contentType(ContentType.JSON)
                .post(BASE_URI + "/api/v1/deal/" + dealId + "/prepare")
                .then().log().all()
                .statusCode(200)
                .extract().response();
    }

    // Перевод сделки в статус Согласование
    private void moveDealToApproval() {
        given().log().body()
                .header("Authorization", "Bearer " + BaseApiTestConfig.TOKEN)
                .when()
                .contentType(ContentType.JSON)
                .post(BASE_URI + "/api/v1/deal/" + dealId + "/coordinate")
                .then().log().all()
                .statusCode(200)
                .extract().response();
    }

    // Перевод сделки в статус Проверка договора
    private void moveDealToContractReview() {
        given().log().body()
                .header("Authorization", "Bearer " + BaseApiTestConfig.TOKEN)
                .when()
                .contentType(ContentType.JSON)
                .post(BASE_URI + "/api/v1/deal/" + dealId + "/check_ddu")
                .then().log().all()
                .statusCode(200)
                .extract().response();
    }

    // Сгенерировать первичный документ
    private void generatePrimaryDocument() {
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
                .statusCode(200)
                .extract().response();
    }

    // Перевод сделки в статус Подписание
    private void moveDealToSigning() {
        given().log().body()
                .header("Authorization", "Bearer " + BaseApiTestConfig.TOKEN)
                .when()
                .contentType(ContentType.JSON)
                .post(BASE_URI + "/api/v1/deal/" + dealId + "/signing")
                .then().log().all()
                .statusCode(200)
                .extract().response();
    }

    // Перевод сделки в статус Подписан
    private void moveDealToSigned() {
        given().log().body()
                .header("Authorization", "Bearer " + BaseApiTestConfig.TOKEN)
                .when()
                .contentType(ContentType.JSON)
                .post(BASE_URI + "/api/v1/deal/" + dealId + "/sign")
                .then().log().all()
                .statusCode(200)
                .extract().response();
    }

    // Перевод сделки в статус Готов к регистрации
    private void moveDealToReadyForRegistration() {
        given().log().body()
                .header("Authorization", "Bearer " + BaseApiTestConfig.TOKEN)
                .when()
                .contentType(ContentType.JSON)
                .post(BASE_URI + "/api/v1/deal/" + dealId + "/readyForRegistration")
                .then().log().all()
                .statusCode(200)
                .extract().response();
    }

    // Перевод сделки в статус Отправлен на регистрацию
    private void moveDealToSentForRegistration() {
        given().log().body()
                .header("Authorization", "Bearer " + BaseApiTestConfig.TOKEN)
                .when()
                .contentType(ContentType.JSON)
                .post(BASE_URI + "/api/v1/deal/" + dealId + "/sentForRegistration")
                .then().log().all()
                .statusCode(200)
                .extract().response();
    }

    // Загрузить документ выписка из ЕГРН
    private void uploadEGRNDocument() throws IOException {
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
                .statusCode(200)
                .extract().response();
    }

    // Перевод сделки в статус Зарегистрирован
    private void moveDealToRegistered() {
        given().log().body()
                .header("Authorization", "Bearer " + BaseApiTestConfig.TOKEN)
                .when()
                .contentType(ContentType.JSON)
                .post(BASE_URI + "/api/v1/deal/" + dealId + "/registered")
                .then().log().all()
                .statusCode(200)
                .extract().response();
    }

    // Перевод сделки в статус Контроль оплаты по графику
    private void moveDealToPaymentScheduleControl() {

        given().log().body()
                .header("Authorization", "Bearer " + BaseApiTestConfig.TOKEN)
                .when()
                .contentType(ContentType.JSON)
                .post(BASE_URI + "/api/v1/deal/" + dealId + "/control")
                .then().log().all()
                .statusCode(200)
                .extract().response();
    }

    // Пополнение Эскроу-счета
    private void topUpEscrowAccount() throws IOException {
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
                .statusCode(200)
                .extract().response();
    }
}



