package com.sdet.projects.datadriven;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoginDataDrivenTest {

    private final LoginValidator validator = new LoginValidator();

    static Stream<LoginData> loginCases() {
        return LoginDataFactory.defaultUsers().stream();
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("loginCases")
    void shouldEvaluateEachJsonLoginRow(LoginData data) {
        assertEquals(data.expectedValid(), validator.isValid(data), data.toString());
    }

    @Test
    void factoryLoadsValidAndInvalidRows() {
        List<LoginData> rows = LoginDataFactory.defaultUsers();

        assertEquals(3, rows.size());
        assertTrue(rows.stream().anyMatch(LoginData::expectedValid));
        assertTrue(rows.stream().anyMatch(row -> !row.expectedValid()));
        assertFalse(rows.get(0).toString().contains("correct-password"));
    }
}
