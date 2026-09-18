package com.sdet.projects.selenium;

/**
 * Small driver contract used by page objects. Production code can wrap a real
 * Selenium {@code WebDriver}; tests use {@link FakeDriver}.
 */
public interface UiDriver {

    BrowserType browserType();

    void get(String url);

    String currentUrl();

    UiElement findById(String id);

    void quit();

    boolean isQuit();
}
