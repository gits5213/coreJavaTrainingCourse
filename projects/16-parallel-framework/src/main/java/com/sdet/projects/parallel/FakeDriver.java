package com.sdet.projects.parallel;

/**
 * Stand-in for Selenium {@code WebDriver}. Identity (not equals) is what parallel
 * tests must isolate.
 */
public final class FakeDriver {

    private boolean quit;

    public void quit() {
        quit = true;
    }

    public boolean isQuit() {
        return quit;
    }
}
