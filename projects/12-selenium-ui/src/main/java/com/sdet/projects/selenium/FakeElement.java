package com.sdet.projects.selenium;

public final class FakeElement implements UiElement {

    private final String id;
    private final StringBuilder value = new StringBuilder();
    private boolean clicked;

    public FakeElement(String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }

    @Override
    public void clear() {
        value.setLength(0);
    }

    @Override
    public void sendKeys(String text) {
        if (text != null) {
            value.append(text);
        }
    }

    @Override
    public void click() {
        clicked = true;
    }

    @Override
    public String value() {
        return value.toString();
    }

    @Override
    public boolean clicked() {
        return clicked;
    }
}
