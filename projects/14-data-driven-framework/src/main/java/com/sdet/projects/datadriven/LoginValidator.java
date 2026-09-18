package com.sdet.projects.datadriven;

/**
 * In-memory login rule so data-driven tests do not need a browser or network.
 */
public final class LoginValidator {

    static final String VALID_USERNAME = "valid.user";
    static final String VALID_PASSWORD = "correct-password";

    public boolean isValid(LoginData data) {
        return VALID_USERNAME.equals(data.username()) && VALID_PASSWORD.equals(data.password());
    }
}
