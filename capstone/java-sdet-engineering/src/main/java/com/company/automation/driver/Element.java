package com.company.automation.driver;

public interface Element {

    void clear();

    void sendKeys(String text);

    void click();

    String value();

    boolean clicked();
}
