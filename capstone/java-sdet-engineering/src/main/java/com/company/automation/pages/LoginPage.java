package com.company.automation.pages;

import com.company.automation.driver.Driver;
import com.company.automation.driver.Element;
import com.company.automation.models.LoginData;

/**
 * Login screen. Locators stay here (conceptually {@code By.id("username")}).
 */
public final class LoginPage {

    static final String USERNAME_ID = "username";
    static final String PASSWORD_ID = "password";
    static final String SUBMIT_ID = "login";

    private final Driver driver;

    public LoginPage(Driver driver) {
        this.driver = driver;
    }

    public void open(String baseUrl) {
        driver.get(baseUrl + "/login");
    }

    public void enterUsername(String username) {
        Element field = driver.findById(USERNAME_ID);
        field.clear();
        field.sendKeys(username);
    }

    public void enterPassword(String password) {
        Element field = driver.findById(PASSWORD_ID);
        field.clear();
        field.sendKeys(password);
    }

    public void submit() {
        driver.findById(SUBMIT_ID).click();
    }

    public void login(LoginData data) {
        enterUsername(data.username());
        enterPassword(data.password());
        submit();
    }
}
