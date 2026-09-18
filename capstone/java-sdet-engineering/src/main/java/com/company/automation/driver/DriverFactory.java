package com.company.automation.driver;

public interface DriverFactory {

    Driver create(BrowserType type);
}
