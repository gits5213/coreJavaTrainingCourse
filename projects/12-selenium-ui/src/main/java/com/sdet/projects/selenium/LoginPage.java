package com.sdet.projects.selenium;

/**
 * Page object for a login form. Locators match the-internet.herokuapp.com/login
 * conceptually: {@code By.id("username")}, {@code By.id("password")},
 * and a submit control treated as {@code By.id("login")}.
 */
public final class LoginPage {

    static final String USERNAME_ID = "username";
    static final String PASSWORD_ID = "password";
    static final String SUBMIT_ID = "login";

    private final UiDriver driver;

    public LoginPage(UiDriver driver) {
        this.driver = driver;
    }

    public void open(String url) {
        driver.get(url);
    }

    public void enterUsername(String username) {
        UiElement field = driver.findById(USERNAME_ID);
        field.clear();
        field.sendKeys(username);
    }

    public void enterPassword(String password) {
        UiElement field = driver.findById(PASSWORD_ID);
        field.clear();
        field.sendKeys(password);
    }

    public void submit() {
        driver.findById(SUBMIT_ID).click();
    }

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        submit();
    }
}
