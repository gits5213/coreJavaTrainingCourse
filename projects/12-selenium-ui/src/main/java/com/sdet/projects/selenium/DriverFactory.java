package com.sdet.projects.selenium;

public interface DriverFactory {

    UiDriver create(BrowserType type);
}
