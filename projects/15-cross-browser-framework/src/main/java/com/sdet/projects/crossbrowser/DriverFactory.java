package com.sdet.projects.crossbrowser;

public interface DriverFactory {

    FakeDriver create(BrowserType type);
}
