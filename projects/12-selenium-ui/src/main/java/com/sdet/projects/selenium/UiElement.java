package com.sdet.projects.selenium;

public interface UiElement {

    void clear();

    void sendKeys(String text);

    void click();

    String value();

    boolean clicked();
}
