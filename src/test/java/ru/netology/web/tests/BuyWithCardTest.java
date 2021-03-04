package ru.netology.web.tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.val;
import org.junit.jupiter.api.*;
import ru.netology.web.data.DataHelper;
import ru.netology.web.dbUtils.DbRequest;
import ru.netology.web.pages.StartingPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BuyWithCardTest {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void shouldOpen() {
        String sutUrl = System.getProperty("sut.url");
        open(sutUrl);
    }

    @AfterEach
    void shouldClearAll() {
        DbRequest.shouldDeleteAfterPayment();
    }

    @Test
    void shouldBuySuccessfullyWithApprovedCard() {
        val startingPage = new StartingPage();
        val buyWithCardPage = startingPage.buyWithCard();
        val number = DataHelper.getApprovedCardNumber();
        buyWithCardPage.withCardNumber(number);
        buyWithCardPage.waitSuccessMessage();
        val paymentInfo = DbRequest.getPaymentInfo();
        assertEquals("APPROVED", paymentInfo.getStatus());

    }

    @Test
    void shouldNotSellWithDeclinedCard() {
        val startingPage = new StartingPage();
        val buyWithCardPage = startingPage.buyWithCard();
        val number = DataHelper.getDeclinedCardNumber();
        buyWithCardPage.withCardNumber(number);
        buyWithCardPage.waitErrorMessage();
        val paymentInfo = DbRequest.getPaymentInfo();
        assertEquals("DECLINED", paymentInfo.getStatus());
    }
}
