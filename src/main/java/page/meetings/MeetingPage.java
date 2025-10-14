package page.meetings;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import page.BasePage;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class MeetingPage extends BasePage {
    // кнопка перехода к странице создания дела
    private static final SelenideElement CREATE_MEETING_BUTTON = $(byText("Создать Дело"));
    // локатор id дела в таблице с делами
    private static final SelenideElement MEETING_ID = $x(".//tr[@class = 'clickable-JNIQAI']//td[@data-cell-name='id']/div");
    // локатор наименования дела в таблице с делами
    private static final SelenideElement MEETING_NAME = $x(".//tr[@class = 'clickable-JNIQAI']//td[@data-cell-name='topic']/div");

    @Step("Выбор кнопки 'Создать дело'")
    public CreateMeetingPage openCreateMeeting() {
        return openPage(CREATE_MEETING_BUTTON, CreateMeetingPage.class);
    }

    @Step("Достаём текст названия созданного дела")
    public String getNameMeeting() {
        return MEETING_NAME.shouldBe(visible).getText();
    }

    @Step("Достаём id дела в таблице")
    public String getIdMeeting() {
        return MEETING_ID.shouldBe(visible).getText();
    }
}
