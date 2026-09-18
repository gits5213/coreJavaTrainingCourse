package com.company.automation.database;

import com.company.automation.models.User;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class InMemoryUserDao implements UserDao {

    private final Map<String, User> users = new ConcurrentHashMap<>();

    public void save(User user) {
        users.put(user.username(), user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(users.get(username));
    }
}
