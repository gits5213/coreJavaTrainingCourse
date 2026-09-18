package com.sdet.projects.jdbc;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DatabaseValidatorTest {

    @Test
    void inMemoryDatabaseHasExpectedUsers() throws SQLException, IOException {
        ValidationReport report = new DatabaseValidator().validate();

        assertEquals(3, report.actualCount());
        assertTrue(report.usernameFound());
        assertTrue(report.passed());
    }
}
