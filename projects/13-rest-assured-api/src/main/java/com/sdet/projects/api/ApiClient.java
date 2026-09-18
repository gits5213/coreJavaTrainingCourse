package com.sdet.projects.api;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

/**
 * Thin wrapper around REST Assured so tests share a base URI instead of copying it.
 */
public final class ApiClient {

    private final String baseUri;

    public ApiClient(String baseUri) {
        this.baseUri = baseUri;
    }

    public String baseUri() {
        return baseUri;
    }

    public Response getUser(int id) {
        return given()
                .baseUri(baseUri)
                .when()
                .get("/users/{id}", id);
    }

    public User fetchUser(int id) {
        Response response = getUser(id);
        response.then().statusCode(200);
        return new User(response.path("username"), response.path("role"));
    }
}
