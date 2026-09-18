package com.sdet.projects.http;

public class NonSuccessStatusException extends RuntimeException {

    private final int statusCode;
    private final String url;

    public NonSuccessStatusException(String url, int statusCode) {
        super("Expected HTTP 200 from " + url + " but got " + statusCode);
        this.url = url;
        this.statusCode = statusCode;
    }

    public int statusCode() {
        return statusCode;
    }

    public String url() {
        return url;
    }
}
