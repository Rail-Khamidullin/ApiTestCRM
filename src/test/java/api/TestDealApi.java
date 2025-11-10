package api;

import api.clients.BaseApiTestConfig;
import api.clients.TestData;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.IOException;

public class TestDealApi extends BaseApiTestConfig {

    private String interestId;            // id Лида
    private int contactId;                // id Контакта
    private int objectId;                 // id Объекта
    private int dealId;                   // id Сделки
    private float objectDiscountAmount;   // булевое значение заполненности сделки для перевода в статус "Оплачено"
    TestData testData = new TestData();

    @Test
    @DisplayName("Создание Лида, перевод лида в интерес, создание сделки, перевод сделки в статус Оплачено")
    public void testDataApi() throws IOException {
        objectId = 2220;
        for (int i = 0; i < 1; i++) {
            Response responseCreate = testData.createInterest();                   // Создание Лида
            interestId = testData.getInterestId(responseCreate);

            Response responseConvert = testData.convertLeadToInterest(interestId); // Перевод лида в статус интерес
            contactId = testData.getContactId(responseConvert);

            testData.createContactDetails(contactId);                              // Создание реквизитов контакту
            testData.addToFavorites(objectId, interestId);                         // Добавление в избраное ОН

            Response responseDeal = testData.createDeal(objectId, interestId);     // Создание сделки
            dealId = testData.getDealId(responseDeal);

            Response responseUpdate = testData.updateAllFields(dealId);            // Заполнение всех полей, чтобы сделку перевести в оплачено
            objectDiscountAmount = testData.getObjectDiscount(responseUpdate);

            testData.moveDealToPreparation(dealId);                                // Перевод сделки в статус Подготовка
            testData.calculateSchedulePayments(dealId);                            // Сформировать график платежей
            testData.moveDealToApproval(dealId);                                   // Перевод сделки в статус Согласование
            testData.moveDealToContractReview(dealId);                             // Перевод сделки в статус Проверка договора
            testData.generatePrimaryDocument(dealId);                              // Сгенерировать первичный документ
            testData.moveDealToSigning(dealId);                                    // Перевод сделки в статус Подписание
            testData.moveDealToSigned(dealId);                                     // Перевод сделки в статус Подписан
            testData.moveDealToReadyForRegistration(dealId);                       // Перевод сделки в статус Готов к регистрации
            testData.moveDealToSentForRegistration(dealId);                        // Перевод сделки в статус Отправлен на регистрацию
            testData.uploadEGRNDocument(contactId, dealId);                        // Загрузить документ выписка из ЕГРН
            testData.moveDealToRegistered(dealId);                                 // Перевод сделки в статус Зарегистрирован
            testData.moveDealToPaymentScheduleControl(dealId);                     // Перевод сделки в статус Контроль оплаты по графику
            testData.topUpEscrowAccount(dealId, objectDiscountAmount);             // Пополнение Эскроу-счета
            objectId++;
        }
    }
}
