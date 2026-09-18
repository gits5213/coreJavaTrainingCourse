package com.sdet.projects.http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class JsonPlaceholderClient {

    public static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    private final HttpClient httpClient;
    private final UserJsonParser parser;

    public JsonPlaceholderClient() {
        this(HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build(), new UserJsonParser());
    }

    JsonPlaceholderClient(HttpClient httpClient, UserJsonParser parser) {
        this.httpClient = httpClient;
        this.parser = parser;
    }

    public User fetchUser(int id) throws IOException, InterruptedException {
        String url = BASE_URL + "/users/" + id;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(15))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        if (status != 200) {
            throw new NonSuccessStatusException(url, status);
        }
        return parser.parse(response.body());
    }
}
