package com.company.automation.models;

/**
 * Login credentials. {@link #toString()} never prints the password.
 */
public record LoginData(String username, String password) {

    @Override
    public String toString() {
        return "LoginData[username=" + username + ", password=***]";
    }
}
