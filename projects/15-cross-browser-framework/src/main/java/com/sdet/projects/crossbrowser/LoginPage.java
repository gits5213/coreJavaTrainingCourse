package com.sdet.projects.crossbrowser;

public final class LoginPage {

    private final FakeDriver driver;

    public LoginPage(FakeDriver driver) {
        this.driver = driver;
    }

    public void open(String url) {
        driver.get(url);
    }

    public void enterUsername(String username) {
        FakeElement field = driver.findById("username");
        field.clear();
        field.sendKeys(username);
    }

    public void enterPassword(String password) {
        FakeElement field = driver.findById("password");
        field.clear();
        field.sendKeys(password);
    }

    public void submit() {
        driver.findById("login").click();
    }
}
