package web;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;

public class AutorizationPage {

    private final SelenideElement inputEmail = $x("//input[@id='email']");
    private final SelenideElement inputPassword = $x("//input[@id='password']");
    private final SelenideElement buttonSubmit = $x("//button[@type='submit']");
    private static final SelenideElement SERVER_ERROR = $x(".//p[text() = 'В настоящий момент соединение с сервером потеряно!']");

    public AutorizationPage(String url) {
        Selenide.open(url);
    }

    public MainPage authorization(String login, String password){
        inputEmail.val(login);
        inputPassword.val(password);
        buttonSubmit.click();

        sleep(5000);
        if (SERVER_ERROR.isDisplayed()) {
            // в случае появлении ошибки ожидаем до 5 секунд, проверяя каждые 500мс, исчезла ли ошибка
            Wait().withTimeout(Duration.ofSeconds(5))
                    .pollingEvery(Duration.ofMillis(500))
                    .until(d -> !SERVER_ERROR.isDisplayed());
            return new MainPage();
        }
        return new MainPage();
    }
}
