package web;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import page.BasePage;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;

public class MainPage extends BasePage{

    private SelenideElement hrefSearch = $x("//input[@placeholder='Поиск...']");
    public static SelenideElement hrefMeeting = $x("//a[@href='/meetings']");
    private SelenideElement hrefContacts = $x("//a[@href='/contacts']");
    public static SelenideElement hrefInterests = $x("//a[@href='/interests']");

    public ContactsPage openContactsPage(){
        hrefContacts.shouldBe(visible).click();
        return new ContactsPage();
    }

    // проверяем отображение выбранной сущности и тапаем на неё
    @Step("Открытие сущности")
    @Override
    public <T extends BasePage> T openPage(SelenideElement pageElement, Class<T> pageClass) {
        return super.openPage(pageElement, pageClass);
    }
}
