package com.sdet.projects.api;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserApiTest {

    private static LocalUserServer server;
    private static ApiClient client;

    @BeforeAll
    static void startServer() throws IOException {
        server = new LocalUserServer();
        client = new ApiClient(server.baseUri());
    }

    @AfterAll
    static void stopServer() {
        if (server != null) {
            server.close();
        }
    }

    @Test
    void shouldReturnJohnAsAdminFromLocalServer() {
        given()
                .baseUri(client.baseUri())
        .when()
                .get("/users/1")
        .then()
                .statusCode(200)
                .body("username", equalTo("john"))
                .body("role", equalTo("admin"));
    }

    @Test
    void apiClientMapsJsonToUserRecord() {
        User user = client.fetchUser(1);

        assertEquals("john", user.username());
        assertEquals("admin", user.role());
    }
}
