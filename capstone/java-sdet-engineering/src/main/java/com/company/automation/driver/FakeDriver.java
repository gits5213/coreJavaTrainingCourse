package com.company.automation.driver;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Default driver for unit/smoke tests. Live Selenium is a later evolution.
 */
public final class FakeDriver implements Driver {

    private final BrowserType browserType;
    private final Map<String, FakeElement> elements = new LinkedHashMap<>();
    private String currentUrl = "";
    private boolean quit;

    public FakeDriver(BrowserType browserType) {
        this.browserType = browserType;
        elements.put("username", new FakeElement());
        elements.put("password", new FakeElement());
        elements.put("login", new FakeElement());
        elements.put("logout", new FakeElement());
    }

    @Override
    public BrowserType browserType() {
        return browserType;
    }

    @Override
    public void get(String url) {
        ensureOpen();
        currentUrl = url;
    }

    @Override
    public String currentUrl() {
        return currentUrl;
    }

    @Override
    public Element findById(String id) {
        ensureOpen();
        return elements.computeIfAbsent(id, ignored -> new FakeElement());
    }

    @Override
    public void quit() {
        quit = true;
        elements.clear();
    }

    @Override
    public boolean isQuit() {
        return quit;
    }

    private void ensureOpen() {
        if (quit) {
            throw new IllegalStateException("Driver already quit");
        }
    }
}
