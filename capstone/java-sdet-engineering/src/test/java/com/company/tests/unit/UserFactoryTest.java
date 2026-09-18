package com.company.tests.unit;

import com.company.automation.config.Config;
import com.company.automation.driver.BrowserType;
import com.company.automation.factories.UserFactory;
import com.company.automation.models.LoginData;
import com.company.automation.utils.SafeLogger;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class UserFactoryTest {

    @Test
    void adminFactoryHidesPasswordInToString() {
        LoginData admin = UserFactory.admin();

        assertEquals("admin", admin.username());
        assertFalse(admin.toString().contains("correct-password"));
    }

    @Test
    void safeLoggerRedactsSecrets() {
        String redacted = SafeLogger.redact("password=correct-password token=abc123");

        assertEquals("password=*** token=***", redacted);
    }

    @Test
    void qaConfigDefaultsToChrome() {
        Config config = Config.qa();

        assertEquals(BrowserType.CHROME, config.browser());
        assertEquals("https://qa.example.test", config.baseUrl());
    }
}
