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
        val buyWithCreditPage = startingPage.buyWithCredit();
        val number = DataHelper.getApprovedCardNumber();
        buyWithCreditPage.withCardNumber(number);
        buyWithCreditPage.waitSuccessMessage();
        val paymentWithCreditInfo = DbRequest.getPaymentWithCreditInfo();
        assertEquals("APPROVED", paymentWithCreditInfo.getStatus());
    }

    @Test
    void shouldNotSellWithDeclinedCard() {
        val startingPage = new StartingPage();
        val buyWithCreditPage = startingPage.buyWithCredit();
        val number = DataHelper.getDeclinedCardNumber();
        buyWithCreditPage.withCardNumber(number);
        buyWithCreditPage.waitErrorMessage();
        val paymentWithCreditInfo = DbRequest.getPaymentWithCreditInfo();
        assertEquals("DECLINED", paymentWithCreditInfo.getStatus());
    }

    @Test
    void shouldNotSellWhenAllFieldsAreEmpty() {
        val startingPage = new StartingPage();
        val buyWithCreditPage = startingPage.buyWithCredit();
        buyWithCreditPage.emptyFields();
        buyWithCreditPage.waitErrorMessageAboutWrongFormat();
        buyWithCreditPage.waitErrorMessageAboutWrongFormat();
        buyWithCreditPage.waitErrorMessageAboutWrongFormat();
        buyWithCreditPage.waitErrorMessageBecauseOfEmptyField();
        buyWithCreditPage.waitErrorMessageAboutWrongFormat();
    }

    @Test
    void shouldNotSellWhenCardValidationCodeIsTwoDigitsShort() {
        val startingPage = new StartingPage();
        val buyWithCreditPage = startingPage.buyWithCredit();
        val cvc = "7";
        buyWithCreditPage.withCardValidationCode(cvc);
        buyWithCreditPage.waitErrorMessageAboutWrongFormat();
    }

    @Test
    void shouldNotSellWhenCardValidationCodeIsOneDigitShort() {
        val startingPage = new StartingPage();
        val buyWithCreditPage = startingPage.buyWithCredit();
        val cvc = "44";
        buyWithCreditPage.withCardValidationCode(cvc);
        buyWithCreditPage.waitErrorMessageAboutWrongFormat();
    }

    @Test
    void shouldNotSellWhenNameOfCardholderIsOnlyLastName() {
        val startingPage = new StartingPage();
        val buyWithCreditPage = startingPage.buyWithCredit();
        val nameOfCardHolder = DataHelper.getOnlyUsersLastName();
        buyWithCreditPage.withCardholder(nameOfCardHolder);
        buyWithCreditPage.waitErrorMessageAboutWrongFormat();
    }

    @Test
    void shouldNotSellWhenNameOfCardholderIsOnlyFirstName() {
        val startingPage = new StartingPage();
        val buyWithCreditPage = startingPage.buyWithCredit();
        val nameOfCardHolder = DataHelper.getOnlyUsersFirstName();
        buyWithCreditPage.withCardholder(nameOfCardHolder);
        buyWithCreditPage.waitErrorMessageAboutWrongFormat();
    }

    @Test
    void shouldNotSellWhenYearNumberIsLowerThanAllowed() {
        val startingPage = new StartingPage();
        val buyWithCreditPage = startingPage.buyWithCredit();
        val yearNumber = "18";
        buyWithCreditPage.withYear(yearNumber);
        buyWithCreditPage.waitErrorMessageWithDateOfExpiry();
    }

    @Test
    void shouldNotSellWhenYearNumberExceedsTheAllowed() {
        val startingPage = new StartingPage();
        val buyWithCreditPage = startingPage.buyWithCredit();
        val yearNumber = "29";
        buyWithCreditPage.withYear(yearNumber);
        buyWithCreditPage.waitErrorMessageAboutWrongDateOfExpiry();
    }

    @Test
    void shouldNotSellWhenYearNumberIsOneDigitalShort() {
        val startingPage = new StartingPage();
        val buyWithCreditPage = startingPage.buyWithCredit();
        val yearNumber = "5";
        buyWithCreditPage.withYear(yearNumber);
        buyWithCreditPage.waitErrorMessageAboutWrongFormat();
    }

    @Test
    void shouldNotSellWhenYearNumberIsZeros() {
        val startingPage = new StartingPage();
        val buyWithCreditPage = startingPage.buyWithCredit();
        val yearNumber = "00";
        buyWithCreditPage.withYear(yearNumber);
        buyWithCreditPage.waitErrorMessageWithDateOfExpiry();
    }

    @Test
    void shouldNotSellWhenCardNumberIsZeros() {
        val startingPage = new StartingPage();
        val buyWithCreditPage = startingPage.buyWithCredit();
        val number = "0000 0000 0000 0000";
        buyWithCreditPage.withCardNumber(number);
        buyWithCreditPage.waitErrorMessage();
    }

    @Test
    void shouldNotSellWhenCardNumberIsUnknown() {
        val startingPage = new StartingPage();
        val buyWithCreditPage = startingPage.buyWithCredit();
        val number = "3251 4687 9856 1245";
        buyWithCreditPage.withCardNumber(number);
        buyWithCreditPage.waitErrorMessage();
    }

    @Test
    void shouldNotSellWhenCardNumberIsShort() {
        val startingPage = new StartingPage();
        val buyWithCreditPage = startingPage.buyWithCredit();
        val number = "3256 5587 5645 22";
        buyWithCreditPage.withCardNumber(number);
        buyWithCreditPage.waitErrorMessageAboutWrongFormat();
    }

    @Test
    void shouldNotSellWhenMonthNumberIsZeros() {
        val startingPage = new StartingPage();
        val buyWithCreditPage = startingPage.buyWithCredit();
        val monthNumber = "00";
        buyWithCreditPage.withMonth(monthNumber);
        buyWithCreditPage.waitErrorMessageAboutWrongDateOfExpiry();
    }

    @Test
    void shouldNotSellWhenMonthNumberIsOneDigitShort() {
        val startingPage = new StartingPage();
        val buyWithCreditPage = startingPage.buyWithCredit();
        val monthNumber = "2";
        buyWithCreditPage.withMonth(monthNumber);
        buyWithCreditPage.waitErrorMessageAboutWrongFormat();
    }

    @Test
    void shouldNotSellWhenMonthNumberExceedsTheAllowed() {
        val startingPage = new StartingPage();
        val buyWithCreditPage = startingPage.buyWithCredit();
        val monthNumber = "15";
        buyWithCreditPage.withMonth(monthNumber);
        buyWithCreditPage.waitErrorMessageAboutWrongDateOfExpiry();
    }

    @Test
    void shouldNotSellWhenNameOfCardholderIsOnlyOneLetter() {
        val startingPage = new StartingPage();
        val buyWithCreditPage = startingPage.buyWithCredit();
        val nameOfCardHolder = "D";
        buyWithCreditPage.withCardholder(nameOfCardHolder);
        buyWithCreditPage.waitErrorMessageAboutWrongFormat();
    }

    @Test
    void shouldNotSellWhenNameOfCardholderHasLotsOfLetters() {
        val startingPage = new StartingPage();
        val buyWithCreditPage = startingPage.buyWithCredit();
        val nameOfCardHolder = "TGFJVNCMDKELWOQIAJZNDTMDLMREW IWJDNRYFBSYRHFYTVCPQZMSHRBD ";
        buyWithCreditPage.withCardholder(nameOfCardHolder);
        buyWithCreditPage.waitErrorMessageAboutWrongFormat();
    }

    @Test
    void shouldNotSellWhenNameOfCardholderInLowerCaseLetters() {
        val startingPage = new StartingPage();
        val buyWithCreditPage = startingPage.buyWithCredit();
        val nameOfCardHolder = DataHelper.getFullUsersNameInLowCaseLetters();
        buyWithCreditPage.withCardholder(nameOfCardHolder);
        buyWithCreditPage.waitErrorMessageAboutWrongFormat();
    }

    @Test
    void shouldNotSellWhenNameOfCardholderInUpperCaseAndLowerCaseLetters() {
        val startingPage = new StartingPage();
        val buyWithCreditPage = startingPage.buyWithCredit();
        val nameOfCardHolder = DataHelper.getFullUsersNameInUpperCaseAndLowCaseLetters();
        buyWithCreditPage.withCardholder(nameOfCardHolder);
        buyWithCreditPage.waitErrorMessageAboutWrongFormat();
    }

    @Test
    void shouldNotSellWhenNameOfCardholderIsInRussian() {
        val startingPage = new StartingPage();
        val buyWithCreditPage = startingPage.buyWithCredit();
        val nameOfCardHolder = DataHelper.getFullUsersNameInRussian("ru");
        buyWithCreditPage.withCardholder(nameOfCardHolder);
        buyWithCreditPage.waitErrorMessageAboutWrongFormat();
    }
}
