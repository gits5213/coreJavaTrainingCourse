package com.sdet.lessons.chapter50;

/**
 * Firefox is a Browser with its own launch behavior.
 */
public class FirefoxBrowser extends Browser {

    @Override
    public void launch() {
        System.out.println("Launching Firefox");
    }

    @Override
    public String name() {
        return "Firefox";
    }
}
