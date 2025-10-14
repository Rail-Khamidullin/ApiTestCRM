package web;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$x;

public class ContactsPage {
    SelenideElement buttonCreateNewContact = $x("//span[text()='Создать Контакт']");


    public CreateNewContact clickButtonCreateNewContact(){
        buttonCreateNewContact.shouldBe(Condition.enabled).click();
        return new CreateNewContact();
    }



}
