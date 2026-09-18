package com.sdet.lessons.chapter78;

/**
 * Chapter 78 — A record is a compact, immutable data carrier.
 * Java generates the constructor, accessors, {@code equals}, {@code hashCode}, and {@code toString}.
 */
public record LoginData(String username, String password) {
}
