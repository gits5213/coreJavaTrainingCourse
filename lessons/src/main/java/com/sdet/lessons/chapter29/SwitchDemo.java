package com.sdet.lessons.chapter29;

/**
 * Chapter 29 — {@code switch}.
 * Choose one path from a closed list of values, such as browser names.
 */
public class SwitchDemo {

    public static void main(String[] args) {
        System.out.println("=== Chapter 29: switch ===");

        launch("chrome");
        launch("firefox");
        launch("edge");
        launch("safari");
    }

    private static void launch(String browser) {
        System.out.print("browser = " + browser + " → ");
        switch (browser.toLowerCase()) {
            case "chrome":
                System.out.println("Launching Chrome.");
                break;
            case "firefox":
                System.out.println("Launching Firefox.");
                break;
            case "edge":
                System.out.println("Launching Edge.");
                break;
            default:
                System.out.println("Unknown browser. Check the test config.");
                break;
        }
    }
}
