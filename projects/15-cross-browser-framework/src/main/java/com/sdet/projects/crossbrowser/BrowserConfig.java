package com.sdet.projects.crossbrowser;

/**
 * Resolves browser from the {@code browser} system property. Default is chrome.
 */
public final class BrowserConfig {

    public static final String PROPERTY = "browser";

    private BrowserConfig() {
    }

    public static BrowserType resolve() {
        return BrowserType.from(System.getProperty(PROPERTY, "chrome"));
    }
}
