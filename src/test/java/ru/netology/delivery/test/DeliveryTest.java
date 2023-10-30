package ru.netology.delivery.test;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;
import java.util.Locale;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

class DeliveryTest {
    private Faker faker;

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    void setUpAll() {
        faker = new Faker(new Locale("ru"));
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() throws InterruptedException {
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);


        $("[data-test-id=city] input").setValue(DataGenerator.generateCity());
        $(By.xpath("//input[@placeholder='Дата встречи']")).sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $(By.xpath("//input[@placeholder='Дата встречи']")).setValue(firstMeetingDate);
        $("[data-test-id=name] input").setValue(DataGenerator.generateName("ru"));
        $("[data-test-id=phone] input").setValue(DataGenerator.generatePhone());
        $("[data-test-id=agreement]").click();
        $(".button").shouldHave(text("Запланировать")).click();
        $("[data-test-id=success-notification]").shouldHave(visible, text("Успешно!\n Встреча успешно запланирована на\n" + firstMeetingDate));
        $(By.xpath("//input[@placeholder='Дата встречи']")).sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $(By.xpath("//input[@placeholder='Дата встречи']")).setValue(secondMeetingDate);
        $(".button").shouldHave(text("Запланировать")).click();
        $("[data-test-id=replan-notification]").shouldBe(visible);
        $("[data-test-id=replan-notification]").shouldHave(text("Необходимо подтверждение\n У вас уже запланирована встреча на другую дату. Перепланировать?"));
        $("[data-test-id=replan-notification] button").click();
        $("[data-test-id=success-notification]").shouldHave(visible, text("Успешно!\n Встреча успешно запланирована на\n" + secondMeetingDate));
    }
}

