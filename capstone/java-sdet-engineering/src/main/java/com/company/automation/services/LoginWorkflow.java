package com.company.automation.services;

import com.company.automation.api.UserApiClient;
import com.company.automation.models.LoginData;
import com.company.automation.models.User;
import com.company.automation.pages.LoginPage;

/**
 * Workflows exist when a journey spans more than one page or mixes API setup + UI.
 */
public final class LoginWorkflow {

    private final LoginPage loginPage;
    private final UserApiClient users;

    public LoginWorkflow(LoginPage loginPage, UserApiClient users) {
        this.loginPage = loginPage;
        this.users = users;
    }

    public void loginAs(LoginData data) {
        loginPage.login(data);
    }

    public User requireUser(int id) {
        return users.findById(id).orElseThrow(() -> new IllegalStateException("No user " + id));
    }
}
