package page.interests;


import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.TimeoutException;
import page.BasePage;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byId;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class CreateInterestsPage extends BasePage {
    // поле для заполнения имени
    private static final SelenideElement NAME_FIELD = $(byId("e=x=t=e=r=n=a=l=C=o=n=t=a=c=t_f=i=r=s=t=N=a=m=e"));
    // поле для заполнения номера телефона
    private static final SelenideElement PHONE_NUMBER_FIELD = $(byId("e=x=t=e=r=n=a=l=C=o=n=t=a=c=t_c=o=m=m=u=n=i=c=a=t=i=o=n_p=h=o=n=e"));
    // кнопка создания Лида
    private static final SelenideElement CREATE_INTERESTS = $(byText("Создать Лид"));
    // локатор кнопки "Назад" к отображению таблицы с Лидами
    private static final SelenideElement BACK_INTERESTS_BUTTON = $x("//div[@class='ant-page-header-back-button']");

    @Step("Заполнение полей создания лида и выбора кнопки 'Создать Лид'")
    public void createNewInterest() {
        setNameField();
        setPhoneField();
        tapToCreateButton();
        sleep(2000);
        tapToBackButton();
    }

    @Step("Заполнение поля с именем Лида")
    public void setNameField() {
        try {
       NAME_FIELD.shouldBe(visible).sendKeys("Tester");
        } catch (TimeoutException e) {
            throw new RuntimeException("Не удалось заполнить поле с именем Лида", e);
        }
    }

    @Step("Заполнение поля с номером телефона")
    public void setPhoneField() {
        try {
        PHONE_NUMBER_FIELD.shouldBe(visible).sendKeys("+76667456452");
        } catch (TimeoutException e) {
            throw new RuntimeException("Не удалось заполнить поле с номером телефона", e);
        }
    }

    @Step("Выбор кнопки 'Создать Лид'")
    public void tapToCreateButton() {
        try {
        CREATE_INTERESTS.shouldBe(visible).click();
        } catch (TimeoutException e) {
            throw new RuntimeException("Не удалось выбрать кнопку 'Создать Лид'", e);
        }
    }

    @Step("Выбор кнопки возврата к таблице с лидами")
    public void tapToBackButton() {
        try {
        BACK_INTERESTS_BUTTON.shouldBe(visible).click();
        } catch (TimeoutException e) {
            throw new RuntimeException("Не удалось выбрать кнопку возврата к таблице с Лидами", e);
        }
    }
}
