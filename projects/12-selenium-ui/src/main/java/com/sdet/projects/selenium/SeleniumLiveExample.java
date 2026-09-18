package com.sdet.projects.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * Opt-in live Selenium demo. Tests do <strong>not</strong> run this class.
 *
 * <p>Requires a local Chrome install. Run with
 * {@code mvn -q compile exec:java} from this module when you want a real browser.
 */
public final class SeleniumLiveExample {

    private static final String LOGIN_URL = "https://the-internet.herokuapp.com/login";

    private SeleniumLiveExample() {
    }

    public static void main(String[] args) {
        LocalDriverFactory factory = new LocalDriverFactory();
        WebDriver driver = factory.createLiveDriver(BrowserType.CHROME);
        try {
            driver.get(LOGIN_URL);
            driver.findElement(By.id("username")).sendKeys("tomsmith");
            driver.findElement(By.id("password")).sendKeys("SuperSecretPassword!");
            driver.findElement(By.cssSelector("button[type='submit']")).click();
            System.out.println("Live login submitted. Current URL: " + driver.getCurrentUrl());
        } finally {
            driver.quit();
        }
    }
}
