package com.sdet.lessons.chapter50;

/**
 * Chrome is a Browser with its own launch behavior.
 */
public class ChromeBrowser extends Browser {

    @Override
    public void launch() {
        System.out.println("Launching Chrome");
    }

    @Override
    public String name() {
        return "Chrome";
    }
}
