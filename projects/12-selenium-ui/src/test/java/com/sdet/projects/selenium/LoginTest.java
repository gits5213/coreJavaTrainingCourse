package com.sdet.projects.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoginTest {

    private UiDriver driver;
    private LoginPage loginPage;

    @BeforeEach
    void setUp() {
        driver = new FakeDriverFactory().create(BrowserType.CHROME);
        loginPage = new LoginPage(driver);
        loginPage.open("https://the-internet.herokuapp.com/login");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    @Test
    void shouldEnterCredentialsAndSubmitWithFakeDriver() {
        loginPage.enterUsername("demo-user");
        loginPage.enterPassword("demo-pass");
        loginPage.submit();

        assertEquals("https://the-internet.herokuapp.com/login", driver.currentUrl());
        assertEquals("demo-user", driver.findById(LoginPage.USERNAME_ID).value());
        assertEquals("demo-pass", driver.findById(LoginPage.PASSWORD_ID).value());
        assertTrue(driver.findById(LoginPage.SUBMIT_ID).clicked());
    }

    @Test
    void localFactoryDoesNotStartARealBrowserDuringUnitTests() {
        LocalDriverFactory factory = new LocalDriverFactory();

        UnsupportedOperationException error = assertThrows(
                UnsupportedOperationException.class,
                () -> factory.create(BrowserType.CHROME));

        assertTrue(error.getMessage().contains("real browser"));
    }

    @Test
    void fakeFactoryCreatesTheRequestedBrowserType() {
        UiDriver firefox = new FakeDriverFactory().create(BrowserType.FIREFOX);

        assertEquals(BrowserType.FIREFOX, firefox.browserType());
        firefox.quit();
        assertTrue(firefox.isQuit());
    }
}
