package com.sdet.projects.http;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class UserJsonParser {

    private static final Pattern ID = Pattern.compile("\"id\"\\s*:\\s*(\\d+)");
    private static final Pattern NAME = Pattern.compile("\"name\"\\s*:\\s*\"([^\"]+)\"");
    private static final Pattern USERNAME = Pattern.compile("\"username\"\\s*:\\s*\"([^\"]+)\"");

    public User parse(String json) {
        if (json == null || json.isBlank()) {
            throw new IllegalArgumentException("json is required");
        }
        int id = Integer.parseInt(firstGroup(ID, json, "id"));
        String name = firstGroup(NAME, json, "name");
        String username = firstGroup(USERNAME, json, "username");
        return new User(id, name, username);
    }

    private static String firstGroup(Pattern pattern, String json, String field) {
        Matcher matcher = pattern.matcher(json);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Missing JSON field: " + field);
        }
        return matcher.group(1);
    }
}
