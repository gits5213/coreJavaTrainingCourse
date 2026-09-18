package com.sdet.projects.http;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserJsonParserTest {

    private final UserJsonParser parser = new UserJsonParser();

    @Test
    void parseExtractsIdNameAndUsername() {
        String json = """
                {
                  "id": 1,
                  "name": "Leanne Graham",
                  "username": "Bret"
                }
                """;

        User user = parser.parse(json);

        assertEquals(1, user.id());
        assertEquals("Leanne Graham", user.name());
        assertEquals("Bret", user.username());
    }

    @Test
    void parseRejectsMissingUsername() {
        String json = """
                { "id": 1, "name": "Ada" }
                """;

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> parser.parse(json)
        );

        assertEquals("Missing JSON field: username", exception.getMessage());
    }
}
