package com.company.automation.api;

import com.company.automation.models.User;

import java.util.Optional;

public interface UserApiClient {

    Optional<User> findById(int id);
}
