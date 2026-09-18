package com.sdet.lessons.chapter48;

/**
 * LoginPage IS-A BasePage. It reuses open/click/type and adds login.
 */
public class LoginPage extends BasePage {

    public void login(String username, String password) {
        type("username", username);
        type("password", password);
        click("Login");
        System.out.println("Submitted login for " + username + " (demo password " + password + ")");
    }
}
