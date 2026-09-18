package com.company.automation.driver;

public enum BrowserType {
    CHROME,
    FIREFOX,
    EDGE;

    public static BrowserType from(String value) {
        if (value == null || value.isBlank()) {
            return CHROME;
        }
        return BrowserType.valueOf(value.trim().toUpperCase());
    }
}
