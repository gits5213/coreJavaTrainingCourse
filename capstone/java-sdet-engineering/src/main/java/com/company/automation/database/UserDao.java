package com.company.automation.database;

import com.company.automation.models.User;

import java.util.Optional;

/**
 * Verification-side persistence. Implement with JDBC when a training DB exists.
 */
public interface UserDao {

    Optional<User> findByUsername(String username);
}
