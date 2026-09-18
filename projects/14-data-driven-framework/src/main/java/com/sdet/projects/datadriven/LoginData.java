package com.sdet.projects.datadriven;

/**
 * One login attempt. {@link #toString()} never prints the password.
 */
public record LoginData(String username, String password, boolean expectedValid) {

    @Override
    public String toString() {
        return "LoginData[username=" + username + ", password=***, expectedValid=" + expectedValid + "]";
    }
}
