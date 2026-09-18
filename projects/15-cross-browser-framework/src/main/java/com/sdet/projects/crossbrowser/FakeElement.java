package com.sdet.projects.crossbrowser;

public final class FakeElement {

    private final StringBuilder value = new StringBuilder();
    private boolean clicked;

    public void clear() {
        value.setLength(0);
    }

    public void sendKeys(String text) {
        if (text != null) {
            value.append(text);
        }
    }

    public void click() {
        clicked = true;
    }

    public String value() {
        return value.toString();
    }

    public boolean clicked() {
        return clicked;
    }
}
