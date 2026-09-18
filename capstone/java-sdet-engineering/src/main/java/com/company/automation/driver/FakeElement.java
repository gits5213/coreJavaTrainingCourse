package com.company.automation.driver;

public final class FakeElement implements Element {

    private final StringBuilder value = new StringBuilder();
    private boolean clicked;

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
