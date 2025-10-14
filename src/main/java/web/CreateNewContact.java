package web;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$x;

public class CreateNewContact {

    SelenideElement fieldName = $x("//input[@id='f=i=r=s=t=N=a=m=e']");
    SelenideElement fieldPhone = $x("//input[@id='p=h=o=n=e=s']");
    SelenideElement buttonCreateNewContact = $x("//span[text()='Создать Контакт']");
    SelenideElement fieldSberEmployee = $x("//input[@id='questionnaire_sberEmployee']");
    SelenideElement fieldYesSberEmployee = $x("//div[@title='Да']");


    public NewContactCreator createNewContact (){
        fieldName.val("Автотест");
        fieldPhone.val("78877888432");
        fieldSberEmployee.shouldBe(Condition.enabled).click();
        fieldYesSberEmployee.shouldBe(Condition.enabled).click();
        buttonCreateNewContact.shouldBe(Condition.enabled).click();
        return new NewContactCreator();

    }

}
