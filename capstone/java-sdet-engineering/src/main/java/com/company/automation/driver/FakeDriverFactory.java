package com.company.automation.driver;

/**
 * Default factory used by tests. Swap this for a Selenium-backed factory when
 * you opt into a real browser.
 */
public final class FakeDriverFactory implements DriverFactory {

    @Override
    public Driver create(BrowserType type) {
        return new FakeDriver(type);
    }
}
