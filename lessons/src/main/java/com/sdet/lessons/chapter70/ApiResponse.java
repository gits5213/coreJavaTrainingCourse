package com.sdet.lessons.chapter70;

/**
 * A typed envelope: HTTP status plus a body of type {@code T}.
 *
 * @param <T> the type of {@code data}
 */
public class ApiResponse<T> {

    public int status;
    public T data;

    public ApiResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }
}
