package com.sdet.lessons.chapter50;

/**
 * Parent type for browsers. Subclasses override {@code launch()}.
 */
public class Browser {

    public void launch() {
        System.out.println("Launching a generic browser");
    }

    public String name() {
        return "browser";
    }
}
