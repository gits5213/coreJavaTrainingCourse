package com.sdet.lessons.chapter50;

/**
 * Chapter 50 — Polymorphism.
 * A {@code Browser} variable can hold Chrome or Firefox. The override that runs depends on the object.
 */
public class PolymorphismDemo {

    public static void main(String[] args) {
        System.out.println("=== Chapter 50: Polymorphism ===");

        Browser chrome = new ChromeBrowser();
        Browser firefox = new FirefoxBrowser();

        start(chrome);
        start(firefox);
        System.out.println("start() only knows Browser. Each object still launches its own way.");
    }

    private static void start(Browser browser) {
        System.out.print(browser.name() + " → ");
        browser.launch();
    }
}
