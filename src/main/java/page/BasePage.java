package page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;

public class BasePage {

    // универсальный метод, который проверяет отображение кнопки, выбирает её и возвращает необходимый класс
    public <T extends BasePage> T openPage(SelenideElement pageElement, Class<T> pageClass) {
        tapToEntity(pageElement);
        try {
            return pageClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка создания page object", e);
        }
    }

    // поиск элемента и тап по нему
    public void tapToEntity(SelenideElement pageElement) {
        pageElement.shouldBe(visible).click();
    }
}
