package page.meetings;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.TimeoutException;
import page.BasePage;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.sleep;
import static constants.Constants.INTEREST;

public class CreateMeetingPage extends BasePage {

    // локатор поля Тип
    private static final SelenideElement TYPE_SELECT = $x("//div[contains(@class, 'ant-select-selector') " +
            "and .//input[@id='type']]");
    // локатор кнопки выбора типа дела
    private static final SelenideElement MEETING = $x("//div[contains(@class, 'ant-select-item-option') " +
            "and contains(., 'Звонок')]");
    // локатор поля категория
    private static final SelenideElement CATEGORY = $x("//div[contains(@class, 'ant-select-selector') " +
            "and .//input[@id = 'category']]");
    // локатор выбора типа встречи
    private static final SelenideElement OUT_CALL = $x("//div[contains(@class, 'ant-select-item-option')" +
            " and contains(., 'Исходящий звонок')]");
    // локатор поля 'С кем'
    private static final SelenideElement RELATION_WITH = $x(".//input[@id = 'relationWith']");
    // выбор пользователя из списка 'С кем'
    private static final SelenideElement INTEREST_MEETING = $x("//div[@class='rc-virtual-list-holder']" +
            "//div[contains(@class, 'ant-select-item-option') and contains(., '" + INTEREST + "')]");
    // локатор кнопки 'Создать дело'
    private static final SelenideElement CREATE_MEETING_BUTTON = $x("//button[.//span[text()='Создать дело']]");
    // локатор кнопки "Назад" к отображению таблицы с делами
    private static final SelenideElement BACK_MEETING_BUTTON = $x("//div[@class='ant-page-header-back']");

    @Step("Заполнение полей задачи и выбор кнопки 'Создать дело'")
    public void createNewMeeting() {
        choiceType();
        choiceCategory();
        setRelationWith();
        tapToButtonCreateMeeting();
        sleep(2000);
        tapToBackButton();
    }

    @Step("Выбор типа встречи")
    public void choiceType() {
        try {
            // клик по селекту, чтобы открыть dropdown
            TYPE_SELECT.shouldBe(visible).click();
            // ожидание появления dropdown и выбор варианта
            MEETING.shouldBe(visible).click();
        } catch (TimeoutException e) {
            throw new RuntimeException("Не удалось выбрать тип встречи", e);
        }
    }

    @Step("Выбор категории встречи")
    public void choiceCategory() {
        try {
            CATEGORY.shouldBe(visible).click();
            OUT_CALL.shouldBe(visible).click();
        } catch (TimeoutException e) {
            throw new RuntimeException("Не удалось выбрать категорию встречи", e);
        }
    }

    @Step("Заполнение поля 'С кем'")
    public void setRelationWith() {
        try {
            RELATION_WITH.shouldBe(visible).click();
            RELATION_WITH.shouldBe(visible).sendKeys(INTEREST);
            INTEREST_MEETING.shouldBe(visible).click();
        } catch (TimeoutException e) {
            throw new RuntimeException("Не удалось заполнить поле 'С кем'", e);
        }
    }

    @Step("Выбор кнопки 'Создать дело' на странице создания дела")
    public void tapToButtonCreateMeeting() {
        try {
            CREATE_MEETING_BUTTON.shouldBe(visible).click();
        } catch (TimeoutException e) {
            throw new RuntimeException("Не удалось выбрать кнопку 'Создать дело' на странице создания дела", e);
        }
    }

    @Step("Выбор кнопки возврата к таблице с делами")
    public void tapToBackButton() {
        try {
            BACK_MEETING_BUTTON.shouldBe(visible).click();
        } catch (TimeoutException e) {
            throw new RuntimeException("Не удалось выбрать кнопку возврата к таблице с Лидами", e);
        }
    }
}
