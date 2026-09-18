package com.sdet.projects.datadriven;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.List;

public final class LoginDataFactory {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private LoginDataFactory() {
    }

    public static List<LoginData> fromClasspath(String resource) {
        try (InputStream in = LoginDataFactory.class.getClassLoader().getResourceAsStream(resource)) {
            if (in == null) {
                throw new IllegalArgumentException("Missing classpath resource: " + resource);
            }
            return MAPPER.readValue(in, new TypeReference<List<LoginData>>() {
            });
        } catch (IOException e) {
            throw new UncheckedIOException("Could not parse login test data", e);
        }
    }

    public static List<LoginData> defaultUsers() {
        return fromClasspath("testdata/login-users.json");
    }
}
