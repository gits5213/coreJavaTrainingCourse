package com.sdet.projects.http;

public record User(int id, String name, String username) {

    public User {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name is required");
        }
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("username is required");
        }
    }
}
