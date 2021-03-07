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

public class BuyWithCreditTest {

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
        val cvc = "8";
        buyWithCreditPage.withCardValidationCode(cvc);
        buyWithCreditPage.waitErrorMessageAboutWrongFormat();
    }

    @Test
    void shouldNotSellWhenCardValidationCodeIsOneDigitShort() {
        val startingPage = new StartingPage();
        val buyWithCreditPage = startingPage.buyWithCredit();
        val cvc = "22";
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
        val yearNumber = "30";
        buyWithCreditPage.withYear(yearNumber);
        buyWithCreditPage.waitErrorMessageAboutWrongDateOfExpiry();
    }

    @Test
    void shouldNotSellWhenYearNumberIsOneDigitalShort() {
        val startingPage = new StartingPage();
        val buyWithCreditPage = startingPage.buyWithCredit();
        val yearNumber = "8";
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
        val number = "5200 7643 6215 2387";
        buyWithCreditPage.withCardNumber(number);
        buyWithCreditPage.waitErrorMessage();
    }

    @Test
    void shouldNotSellWhenCardNumberIsShort() {
        val startingPage = new StartingPage();
        val buyWithCreditPage = startingPage.buyWithCredit();
        val number = "1268 9875 4561 11";
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
        val monthNumber = "9";
        buyWithCreditPage.withMonth(monthNumber);
        buyWithCreditPage.waitErrorMessageAboutWrongFormat();
    }

    @Test
    void shouldNotSellWhenMonthNumberExceedsTheAllowed() {
        val startingPage = new StartingPage();
        val buyWithCreditPage = startingPage.buyWithCredit();
        val monthNumber = "13";
        buyWithCreditPage.withMonth(monthNumber);
        buyWithCreditPage.waitErrorMessageAboutWrongDateOfExpiry();
    }

    @Test
    void shouldNotSellWhenNameOfCardholderIsOnlyOneLetter() {
        val startingPage = new StartingPage();
        val buyWithCreditPage = startingPage.buyWithCredit();
        val nameOfCardHolder = "T";
        buyWithCreditPage.withCardholder(nameOfCardHolder);
        buyWithCreditPage.waitErrorMessageAboutWrongFormat();
    }

    @Test
    void shouldNotSellWhenNameOfCardholderHasLotsOfLetters() {
        val startingPage = new StartingPage();
        val buyWithCreditPage = startingPage.buyWithCredit();
        val nameOfCardHolder = "IWJDNRYFBSYRHFYTVCPQZMSHRBD TGFJVNCMDKELWOQIAJZNDTMDLMREW";
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