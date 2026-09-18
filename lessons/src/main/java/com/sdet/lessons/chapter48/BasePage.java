package com.sdet.lessons.chapter48;

/**
 * Shared page tools. Child pages inherit these methods.
 */
public class BasePage {

    public void open(String url) {
        System.out.println("Opening " + url);
    }

    public void click(String elementName) {
        System.out.println("Clicking " + elementName);
    }

    public void type(String fieldName, String value) {
        System.out.println("Typing into " + fieldName + ": " + value);
    }
}
