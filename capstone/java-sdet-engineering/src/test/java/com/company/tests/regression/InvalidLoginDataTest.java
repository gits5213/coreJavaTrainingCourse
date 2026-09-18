package com.company.tests.regression;

import com.company.automation.factories.UserFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

class InvalidLoginDataTest {

    @Test
    void invalidFactoryIsNotTheAdminPassword() {
        assertNotEquals(UserFactory.admin().password(), UserFactory.invalid().password());
    }
}
