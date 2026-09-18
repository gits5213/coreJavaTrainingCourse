package com.company.automation.api;

import com.company.automation.models.User;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory API for unit tests. A live client (REST Assured / HttpClient) comes later.
 */
public final class FakeUserApiClient implements UserApiClient {

    private final Map<Integer, User> users = new ConcurrentHashMap<>();

    public FakeUserApiClient() {
        users.put(1, new User(1, "john", "admin"));
    }

    public void put(User user) {
        users.put(user.id(), user);
    }

    @Override
    public Optional<User> findById(int id) {
        return Optional.ofNullable(users.get(id));
    }
}
