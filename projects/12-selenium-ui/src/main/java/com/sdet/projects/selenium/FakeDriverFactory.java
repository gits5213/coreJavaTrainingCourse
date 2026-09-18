package com.sdet.projects.selenium;

public final class FakeDriverFactory implements DriverFactory {

    @Override
    public UiDriver create(BrowserType type) {
        return new FakeDriver(type);
    }
}
