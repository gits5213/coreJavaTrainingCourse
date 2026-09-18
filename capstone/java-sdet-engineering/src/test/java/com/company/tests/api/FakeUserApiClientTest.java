package com.company.tests.api;

import com.company.automation.api.FakeUserApiClient;
import com.company.automation.api.UserApiClient;
import com.company.automation.models.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FakeUserApiClientTest {

    @Test
    void findsSeededAdminUser() {
        UserApiClient client = new FakeUserApiClient();

        User john = client.findById(1).orElseThrow();

        assertEquals("john", john.username());
        assertEquals("admin", john.role());
        assertTrue(client.findById(99).isEmpty());
    }
}
