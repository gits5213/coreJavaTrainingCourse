package com.company.automation.components;

import com.company.automation.driver.Driver;

/**
 * Shared header widget. Components are for controls reused across pages.
 */
public final class HeaderComponent {

    private final Driver driver;

    public HeaderComponent(Driver driver) {
        this.driver = driver;
    }

    public void logout() {
        driver.findById("logout").click();
    }

    public boolean logoutClicked() {
        return driver.findById("logout").clicked();
    }
}
