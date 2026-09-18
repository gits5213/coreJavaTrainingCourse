package com.company.automation.driver;

public interface Driver {

    BrowserType browserType();

    void get(String url);

    String currentUrl();

    Element findById(String id);

    void quit();

    boolean isQuit();
}
