package com.sdet.projects.crossbrowser;

public final class FakeDriverFactory implements DriverFactory {

    @Override
    public FakeDriver create(BrowserType type) {
        return new FakeDriver(type);
    }
}
