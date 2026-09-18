package com.company.tests.ui;

import com.company.automation.driver.BrowserType;
import com.company.automation.driver.Driver;
import com.company.automation.driver.DriverManager;
import com.company.automation.factories.UserFactory;
import com.company.automation.pages.LoginPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoginPageTest {

    private Driver driver;
    private LoginPage loginPage;

    @BeforeEach
    void setUp() {
        driver = DriverManager.create(BrowserType.CHROME);
        loginPage = new LoginPage(driver);
        loginPage.open("https://qa.example.test");
    }

    @AfterEach
    void tearDown() {
        DriverManager.unload();
    }

    @Test
    void loginPageFillsFakeForm() {
        loginPage.login(UserFactory.admin());

        assertEquals("https://qa.example.test/login", driver.currentUrl());
        assertEquals("admin", driver.findById("username").value());
        assertTrue(driver.findById("login").clicked());
    }
}
