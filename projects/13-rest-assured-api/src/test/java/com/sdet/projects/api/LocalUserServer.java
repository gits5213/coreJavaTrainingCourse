package com.sdet.projects.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

/**
 * Loopback-only JSON server so REST Assured tests never need the public internet.
 */
final class LocalUserServer implements AutoCloseable {

    private final HttpServer server;

    LocalUserServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/users/1", this::serveUser);
        server.start();
    }

    String baseUri() {
        return "http://127.0.0.1:" + server.getAddress().getPort();
    }

    private void serveUser(HttpExchange exchange) throws IOException {
        byte[] body = "{\"username\":\"john\",\"role\":\"admin\"}".getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, body.length);
        try (OutputStream out = exchange.getResponseBody()) {
            out.write(body);
        }
    }

    @Override
    public void close() {
        server.stop(0);
    }
}
