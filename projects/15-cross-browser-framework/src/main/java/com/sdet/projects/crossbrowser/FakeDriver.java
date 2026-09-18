package com.sdet.projects.crossbrowser;

import java.util.LinkedHashMap;
import java.util.Map;

public final class FakeDriver {

    private final BrowserType browserType;
    private final Map<String, FakeElement> elements = new LinkedHashMap<>();
    private String currentUrl = "";

    public FakeDriver(BrowserType browserType) {
        this.browserType = browserType;
        elements.put("username", new FakeElement());
        elements.put("password", new FakeElement());
        elements.put("login", new FakeElement());
    }

    public BrowserType browserType() {
        return browserType;
    }

    public void get(String url) {
        currentUrl = url;
    }

    public String currentUrl() {
        return currentUrl;
    }

    public FakeElement findById(String id) {
        return elements.computeIfAbsent(id, ignored -> new FakeElement());
    }
}
