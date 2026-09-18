package com.sdet.lessons.chapter61;

/**
 * Chapter 61 — Enums.
 * Use an enum instead of free-form strings for a small, fixed set of names.
 */
public class EnumDemo {

    public static void main(String[] args) {
        System.out.println("=== Chapter 61: Enum ===");

        launch(BrowserType.CHROME);
        launch(BrowserType.FIREFOX);
        launch(BrowserType.EDGE);

        System.out.println("All values:");
        for (BrowserType type : BrowserType.values()) {
            System.out.println("  " + type);
        }
    }

    private static void launch(BrowserType browserType) {
        switch (browserType) {
            case CHROME:
                System.out.println("Config " + browserType + " → Chrome binary");
                break;
            case FIREFOX:
                System.out.println("Config " + browserType + " → Firefox binary");
                break;
            case EDGE:
                System.out.println("Config " + browserType + " → Edge binary");
                break;
        }
    }
}
