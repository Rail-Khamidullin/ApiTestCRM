package page.interests;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import page.BasePage;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class InterestsPage extends BasePage {
    // кнопка перехода к странице создания Лида
    private static final SelenideElement CREATE_INTERESTS_BUTTON = $(byText("Создать Лид"));
    // локатор id Лида в таблице с Лидами
    private static final SelenideElement INTEREST_ID = $x(".//tr[@class = 'clickable-JNIQAI']//td[@data-cell-name='id']/div");
    // локатор наименования Лида в таблице с Лидами
    private static final SelenideElement INTEREST_NAME = $x(".//tr[@class = 'clickable-JNIQAI']//td[@data-cell-name='name']/div");

    @Step("Выбор кнопки 'Создать Лид'")
    public CreateInterestsPage openCreateInterests() {
        return openPage(CREATE_INTERESTS_BUTTON, CreateInterestsPage.class);
    }

    @Step("Получение id созданного Лида из таблицы")
    public String getIdInterest() {
        return INTEREST_ID.shouldBe(visible).getText();
    }

    @Step("Получение названия созданного Лида из таблицы")
    public String getNameInterest() {
        return INTEREST_NAME.shouldBe(visible).getText();
    }
}
