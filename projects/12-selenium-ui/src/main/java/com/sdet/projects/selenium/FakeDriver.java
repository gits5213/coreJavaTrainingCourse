package com.sdet.projects.selenium;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * In-memory stand-in for a browser. Enough for page-object unit tests.
 */
public final class FakeDriver implements UiDriver {

    private final BrowserType browserType;
    private final Map<String, FakeElement> elements = new LinkedHashMap<>();
    private String currentUrl = "";
    private boolean quit;

    public FakeDriver(BrowserType browserType) {
        this.browserType = browserType;
        elements.put("username", new FakeElement("username"));
        elements.put("password", new FakeElement("password"));
        elements.put("login", new FakeElement("login"));
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
    public UiElement findById(String id) {
        ensureOpen();
        return elements.computeIfAbsent(id, FakeElement::new);
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
