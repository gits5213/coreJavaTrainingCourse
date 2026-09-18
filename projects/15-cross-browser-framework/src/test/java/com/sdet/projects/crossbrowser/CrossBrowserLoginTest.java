package com.sdet.projects.crossbrowser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CrossBrowserLoginTest {

    private final DriverFactory factory = new FakeDriverFactory();

    @ParameterizedTest(name = "login page on {0}")
    @EnumSource(BrowserType.class)
    void loginPageWorksOnEachBrowser(BrowserType type) {
        FakeDriver driver = factory.create(type);
        LoginPage page = new LoginPage(driver);

        page.open("https://qa.example.test/login");
        page.enterUsername("qa.user");
        page.enterPassword("demo-pass");
        page.submit();

        assertEquals(type, driver.browserType());
        assertEquals("qa.user", driver.findById("username").value());
        assertTrue(driver.findById("login").clicked());
    }

    @Test
    void configDefaultsToChrome() {
        String original = System.getProperty(BrowserConfig.PROPERTY);
        try {
            System.clearProperty(BrowserConfig.PROPERTY);
            assertEquals(BrowserType.CHROME, BrowserConfig.resolve());
        } finally {
            restore(original);
        }
    }

    @Test
    void configReadsBrowserSystemProperty() {
        String original = System.getProperty(BrowserConfig.PROPERTY);
        try {
            System.setProperty(BrowserConfig.PROPERTY, "firefox");
            assertEquals(BrowserType.FIREFOX, BrowserConfig.resolve());
        } finally {
            restore(original);
        }
    }

    private static void restore(String original) {
        if (original == null) {
            System.clearProperty(BrowserConfig.PROPERTY);
        } else {
            System.setProperty(BrowserConfig.PROPERTY, original);
        }
    }
}
