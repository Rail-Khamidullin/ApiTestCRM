package web;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$x;

public class NewContactCreator {
    SelenideElement chek = $x("//div[text()='Основное']/following-sibling::div/button");


    public void check(){
        chek.shouldBe(Condition.enabled);
    }
}
