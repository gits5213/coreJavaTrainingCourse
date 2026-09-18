package com.sdet.lessons.chapter48;

/**
 * Chapter 48 — Inheritance.
 * {@code LoginPage extends BasePage} means LoginPage IS-A BasePage.
 */
public class InheritanceDemo {

    public static void main(String[] args) {
        System.out.println("=== Chapter 48: Inheritance ===");

        LoginPage loginPage = new LoginPage();
        loginPage.open("https://example.test/login");
        loginPage.login("qa.tester", "Test123");
        System.out.println("open/click/type came from BasePage. login() came from LoginPage.");
    }
}
