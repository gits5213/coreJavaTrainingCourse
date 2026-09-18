package com.company.tests.smoke;

import com.company.automation.api.FakeUserApiClient;
import com.company.automation.config.Config;
import com.company.automation.driver.Driver;
import com.company.automation.driver.DriverManager;
import com.company.automation.factories.UserFactory;
import com.company.automation.pages.LoginPage;
import com.company.automation.services.LoginWorkflow;
import com.company.automation.services.ScreenshotService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlatformSmokeTest {

    @AfterEach
    void tearDown() {
        try {
            DriverManager.unload();
        } catch (IllegalStateException ignored) {
            // smoke may exit before a driver is bound
        }
    }

    @Test
    void smokeLoginAndFakeApi() throws IOException {
        Config config = Config.qa();
        Driver driver = DriverManager.create(config.browser());
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open(config.baseUrl());

        LoginWorkflow workflow = new LoginWorkflow(loginPage, new FakeUserApiClient());
        workflow.loginAs(UserFactory.admin());

        assertEquals("john", workflow.requireUser(1).username());
        Path evidence = new ScreenshotService(Path.of("target", "evidence")).capture("smoke-login");
        assertTrue(evidence.toFile().exists());
    }
}
