package com.sdet.projects.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * Production-shaped factory. A live {@link ChromeDriver} needs Chrome installed
 * on the machine (Selenium Manager can fetch the driver binary, not the browser).
 * {@code mvn test} must use {@link FakeDriverFactory} instead of {@link #createLiveDriver}.
 */
public final class LocalDriverFactory implements DriverFactory {

    @Override
    public UiDriver create(BrowserType type) {
        throw new UnsupportedOperationException(
                "LocalDriverFactory requires a real browser. Use FakeDriverFactory in tests "
                        + "or call createLiveDriver() from SeleniumLiveExample when you opt in.");
    }

    /**
     * Real Selenium WebDriver. Do not call from unit tests or CI without a browser.
     */
    public WebDriver createLiveDriver(BrowserType type) {
        return switch (type) {
            case CHROME -> new ChromeDriver();
            case FIREFOX -> new FirefoxDriver();
            case EDGE -> new EdgeDriver();
        };
    }
}
