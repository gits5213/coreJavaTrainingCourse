package com.company.automation.factories;

import com.company.automation.models.LoginData;

public final class UserFactory {

    private UserFactory() {
    }

    public static LoginData admin() {
        return new LoginData("admin", "correct-password");
    }

    public static LoginData invalid() {
        return new LoginData("admin", "wrong-password");
    }
}
